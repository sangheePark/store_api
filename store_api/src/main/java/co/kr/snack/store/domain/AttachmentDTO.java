package co.kr.snack.store.domain;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <b></b>
 * <pre>
 * <b>Description:</b>
 * </pre>
 *
 * <pre>
 * <b>History:</b>
 * - 2019.11.22, snack: 최초작성 
 * </pre>
 * @author snack (novles@naver.com)
 * @Version 1.0, 2019.11.22
 */
public class AttachmentDTO {

//    @Getter
//    @Setter
//    public static class UploadFile {
//        private Company company;
//        private List<MultipartFile> bankbook;
//        private List<MultipartFile> bisReg;
//
//        @Builder
//        public UploadFile(Company company, List<MultipartFile> bankbook, List<MultipartFile> bisReg) {
//        	System.out.println("companyInfo = " + company.toString());
//            this.company = company;
//            this.bankbook = bankbook;
//            this.bisReg = bisReg;
//        }
//    }
////    
//    @Getter
//    @Setter
//    @Builder
//    public static class UploadResponse {
//        private String path;
//        private Long fileSize;
//        private String fileSizeStr;
//        private String fileName;
//        private String tempKey;
//        private String type;
//    }
//    
//    @Getter
//    @Setter
//    public static class DownloadResponse {
//        private Resource resource;
//        private String fileName;
//        private MediaType mediaType;
//        private String headerKey;
//        private String headerValue;
//
//        @Builder
//        public DownloadResponse(Resource resource, String fileName) {
//           this.resource = resource;
//           this.fileName = fileName;
//           this.mediaType = MediaType.parseMediaType("application/octet-stream");
//           this.headerKey = HttpHeaders.CONTENT_DISPOSITION;
//           this.headerValue = "attachment; filename=\"" + this.getFileName() + "\"";
//        }
//    }
}
