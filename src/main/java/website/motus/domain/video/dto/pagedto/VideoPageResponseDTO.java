package website.motus.domain.video.dto.pagedto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class VideoPageResponseDTO<E> {

    private int page;
    private int size;
    private int total;

    private int start;
    private int end;

    private boolean prev;
    private boolean next;

    private List<E> dtoList;

    @Builder(builderMethodName = "withAll")
    public VideoPageResponseDTO(VideoPageRequestDTO pageRequestDTO, List<E> dtoList, int total){ //여러 정보를 생성자를 이용해서 받아서 처리하는 것이 안전

        if(total <= 0){
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end =   (int)(Math.ceil(this.page / 10.0 )) *  10; // end page

        this.start = this.end - 9; // start page

        int last =  (int)(Math.ceil((total/(double)size))); // last page total count

        this.end = Math.min(end, last); // If the last page is less than the last value, the last value is end

        this.prev = this.start > 1; // if start > 1, prev page always true

        this.next =  total > this.end * this.size; // Is the total number greater than the product of the last page and the number per page?

    }
}