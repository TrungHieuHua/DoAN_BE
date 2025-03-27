package com.shose.shoseshop.controller;


import com.shose.shoseshop.controller.request.FeedbackRequest;
import com.shose.shoseshop.controller.response.FeedbackResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.FeedBackService;
import com.shose.shoseshop.util.PaginationSortingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedbackController {

    FeedBackService feedBackService;

    @PostMapping
    public ResponseData<FeedbackResponse> creatReview(@RequestBody FeedbackRequest request){
        return new ResponseData<>("Create review success",
                feedBackService.createFeedBack(request));
    }


    @GetMapping("/product/{productId}")
    public ResponseData<Page<FeedbackResponse>> getReviewByProductId(@PathVariable  Long productId
            , @RequestParam(value = "pageNum", defaultValue = "0") int pageNum
            , @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
            , @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir
            , @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy){
        Pageable pageable = PaginationSortingUtils.getPageable(pageNum, pageSize, sortDir, sortBy);

        return new ResponseData<>("Get review by product id success",feedBackService.getReviewByProductId(pageable,productId));
    }

    @GetMapping("/user/{userId}/product/{productId}")
    public ResponseData<FeedbackResponse> getReviewByUserIdAndProductId(@PathVariable Long userId, @PathVariable Long productId){
        return new ResponseData<>("Get review by user id and product id success",
               feedBackService.getReviewByUserAndProduct(userId, productId));
    }
}
