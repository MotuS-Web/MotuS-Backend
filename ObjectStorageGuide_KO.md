# Object Storage API 사용법

<br/>

- 본 문서는 Naver Cloud 에서 서비스하는 Object Storage API 사용법에 대해 기술합니다. 
- 사용 분야는 영상 파일, json파일, 썸네일파일 업로드 입니다. 
- Naver Cloud를 사용하지 않아도 되며 AWS의 S3와 호환됩니다. 
- Naver Cloud의 [Java용 AWS SDK 가이드](https://guide.ncloud-docs.com/docs/storage-storage-8-1) 를 참조하였습니다. 
- Last Edit : 2023.09.30 
 
<br/>

## 왜 사용하여야 하는지?
- 본문에 앞서 Object Storage를 왜 사용하였는지에 대해 언급하고자 합니다. 
- 먼저 저희가 만든 오픈소스의 특성상 Client뿐만 아니라 AI Server와도 데이터를 주고 받습니다. 
- Client에서 BE 서버로 전송한 파일들을 Object Storage에 저장하면 AI Server 및 Client 측에서 이를 사용하는 구조이므로 BE에서 받는 API Request 수를 줄이며, URL 형태로 접근 가능하다는 장점으로 인해 File 전송을 통한 레이턴시가 적어집니다.
- 서버 내부에 파일을 저장한다면 서버 용량 증가 뿐만아니라 추후 서버 증설 시 파일 관리에 어려움이 생깁니다.
- Redis의 Global Cache와 비슷하게게 파일들을 한곳에서 관리하므로 위와 같은 문제들을 해결합니다.

<br/>

## 아키텍쳐
![image](https://github.com/MotuS-Web/MotuS-Backend/assets/52206904/d218f932-a18e-41c9-a5c7-a22643d0653d)

<br/>


## 1. Access-key and Secret-key 발급

- NaverCloud에 로그인 -> 마이페이지 -> 인증키 관리 -> 신규 API 인증키 생성

<br/>

## 2. Object Storage bucket 생성
> [Object Storage 화면 가이드](https://guide.ncloud-docs.com/docs/objectstorage-use-screen)
- 콘솔 -> Services -> Object Storage -> Bucket Management - > 버킷 생성
- 새 폴더 생성 (앞으로 사용하게 될 폴더들을 생성합니다)
- 본 오픈소스에서는 video, json, thumbnail 이라는 폴더 3개를 생성 하였습니다.
<br/>

## 3. application.yml 기입
- 발급받은 키 정보와 region, bucket 이름 등을 spring boot 의 application.yml 에 기입합니다.
- 민감한 정보들이니 별도의 설정 파일을 만들어 main yml에서 불러들이는 방식을 사용합니다.
```yml
cloud:
  aws:
    credentials:
      access-key: "your access-key"
      secret-key: "your secret-key"
    stack:
      auto: false
    region:
      static: ap-northeast-2 // your location -> if you in korea, use ap-northeast-2
    s3:
      endpoint: https://kr.object.ncloudstorage.com
      bucket: "your bucket name"
```

<br/>

## 4. S3Client 설정파일 만들기

- @Configuration을 통해 설정파일을 만들어줍니다. (싱글톤으로 관리하기 위함)
- Amazon의 S3 SDK를 사용하므로 AmazonS3와 연결하기위한 객체를 만들어서 반환 받습니다.
- @Value를 이용해 yml로 작성한 설정파일에서 정보를 불러들입니다.

``` java
@Configuration
public class S3Client {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.endpoint}")
    private String endPoint;

    @Value("${cloud.aws.region.static}")
    private String regionName;

    public AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}
```

<br/>

## 5. API 사용법 설명
>  [Video Service 구현 코드](https://github.com/MotuS-Web/MotuS-Backend/blob/main/src/main/java/com/hallym/rehab/domain/video/service/VideoServiceImpl.java)
- Path와 URL의 차이점
- Path는 Object Storage에서 파일들이 실제 저장되는 경로이며, 삭제 할 경우 이를 이용하여 삭제합니다.
- URL은 사용자가 실제로 Object를 얻고자 할 때 사용되는 URL입니다. 이는 S3 SDK에서 제공하지 않지만 패턴이 일정하므로 직접 만들어 저장합니다.

  <br/>
  
### 5-1 서비스로직에서 사용할 의존성들을 주입받습니다. (bucketName, s3Client, repository 등)
```java
@Value("${cloud.aws.s3.bucket}")
private String bucketName; // your bucketName
private final S3Client s3Client; // connect to S3
private final VideoRepository videoRepository;
```

### 5-2. upload 구현
1. createVideo 메소드 부분에서 MultipartFile로 업로드 할 파일들을 받습니다.
2. uploadFileToS3 메소드를 만들어 video 파일 및 json 파일 들을 매개변수로 전달합니다.
```java 
@Override
public String createVideo(VideoRequestDTO videoRequestDTO) {
    MultipartFile[] files = videoRequestDTO.getFiles();

    MultipartFile videoFile =  files[0];
    MultipartFile jsonFile =  files[1];
    UploadFileDTO uploadFileDTO = uploadFileToS3(videoFile, jsonFile);

    ... 다음 코드들은 8번에서 설명합니다.
}
```

3. DI받은 s3Client의 getAmazonS3를 사용하여 싱글톤 객체를 생성합니다.
```java
AmazonS3 s3 = s3Client.getAmazonS3();
```
4. MultipartFile을 File 객체로 변환해주는 메소드를 통해 File 객체로 변환합니다.
```java
uploadVideoFile = convertMultipartFileToFile(videoFile, videoFileName);
uploadJsonFile = convertMultipartFileToFile(jsonFile, jsonFileName);
...

@Override
public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) {
    File convertedFile = new File(fileName);
    try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
        fos.write(multipartFile.getBytes());
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    return convertedFile;
}

```
5. s3.putObject 메소드를 이용해 원하는 bucket에 Path를 지정해주어 파일을 업로드합니다.
```java
s3.putObject(bucketName, VideoObjectPath, uploadVideoFile);
s3.putObject(bucketName, jsonObjectPath, uploadJsonFile);
s3.putObject(bucketName, thumbnailObjectPath, uploadThumbnailFile);
```
> [Object Storage 권한관리](https://guide.ncloud-docs.com/docs/storage-objectstorage-subaccount)
6. 업로드 한 파일들은 첫 생성 시 사용자로 하여금 접근을 불가능하게 하여 권한을 설정해주어야 합니다.
7. setACL 메소드를 구현하여 방금 생성한 파일들을 접근 가능하도록 설정합니다.
```java
// All User can access Object
setAcl(s3, VideoObjectPath);
setAcl(s3, jsonObjectPath);
setAcl(s3, thumbnailObjectPath);

@Override
public void setAcl(AmazonS3 s3, String objectPath) {
    AccessControlList objectAcl = s3.getObjectAcl(bucketName, objectPath);
    objectAcl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
    s3.setObjectAcl(bucketName, objectPath, objectAcl);
}

```
8. URL, Path등을 만들고 Video Entity를 저장합니다.
```java
@Override
public String createVideo(VideoRequestDTO videoRequestDTO) {
    MultipartFile[] files = videoRequestDTO.getFiles();

    MultipartFile videoFile =  files[0];
    MultipartFile jsonFile =  files[1];
    UploadFileDTO uploadFileDTO = uploadFileToS3(videoFile, jsonFile);

    Video video = videoRequestDTO.toVideo(uploadFileDTO);
    videoRepository.save(video);

    return "success";
}
```

### 5-3 delete 구현
1. Video의 PK를 전달 받습니다.
2. PK를 이용해 Video 객체를 얻고 파일들의 Path를 꺼냅니다.
```java
@Override
public String deleteVideo(Long vno) {
    Optional<Video> byId = videoRepository.findById(vno);
    if (byId.isEmpty()) return "Video not found for Id : " + vno;

    Video video = byId.get();
    String videoPath = video.getVideoPath();
    String jsonPath = video.getJsonPath();
    String thumbnailPath = video.getThumbnailPath();

    deleteFileFromS3(videoPath, jsonPath, thumbnailPath);
    ... 4번에서 설명합니다.
}

```
3. deleteFileFromS3 메소드를 만들어 Path를 이용해 Object Storage에서 삭제합니다.
```java
@Override
public void deleteFileFromS3(String videoPath, String jsonPath, String thumbnailPath) {
    AmazonS3 s3 = s3Client.getAmazonS3();

    try {
        s3.deleteObject(bucketName, videoPath);
        s3.deleteObject(bucketName, jsonPath);
        log.info("Delete Object successfully");
    } catch(SdkClientException e) {
        e.printStackTrace();
        log.info("Error deleteFileFromS3");
    }
}
```
4. Video 객체도 삭제합니다.
```java
@Override
public String deleteVideo(Long vno) {
    Optional<Video> byId = videoRepository.findById(vno);
    if (byId.isEmpty()) return "Video not found for Id : " + vno;

    Video video = byId.get();
    String videoPath = video.getVideoPath();
    String jsonPath = video.getJsonPath();
    String thumbnailPath = video.getThumbnailPath();

    deleteFileFromS3(videoPath, jsonPath, thumbnailPath);
    videoRepository.delete(video); // --> delete from DB

    return "success";
}
```
