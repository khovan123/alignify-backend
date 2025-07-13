
package com.api.dto.request;

import com.api.model.PlanPermission;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class PlanRequest {
    private String planName;
    private String description;
    private String roleId;
    private List<String> permissionIds;
    private List<PlanPermission> planPermissions;
    private Double price;
    private Double discount;
    private String planType;
    private boolean isPopular;
    private boolean isActive;

    public PlanRequest() {
        this.isPopular = false;
        this.isActive = false;
        this.discount = 0.0;
    }
    

}
