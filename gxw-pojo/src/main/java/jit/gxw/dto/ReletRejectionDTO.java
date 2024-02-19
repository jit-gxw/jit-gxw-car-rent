package jit.gxw.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReletRejectionDTO implements Serializable {

    private Long id;

    //审核不通过原因
    private String rejectionReason;


}
