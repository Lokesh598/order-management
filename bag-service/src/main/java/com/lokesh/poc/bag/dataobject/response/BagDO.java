package com.lokesh.poc.bag.dataobject.response;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "bag-summary")
public class BagDO {
    /**
     * @Response:
     * {
     *     bagId:
     *     totalItem:
     *     item: [
     *          {
     *              itemId:
     *              name:
     *              qty:
     *              price:
     *          },
     *          {
     *              itemId:
     *              name:
     *              qty:
     *              price:
     *          }
     *     ]
     * }
     */
    @Id
    private String id;
    private String bagId;
    private int totalItem;
    private List<BagItemDO> bagItem;

    public BagDO(String bagId, List<BagItemDO> bagItemDOList) {
        this.bagId = bagId;
        this.bagItem = bagItemDOList;
    }

    public BagDO(String bagId, int totalItem, List<BagItemDO> bagItemDOList) {
        this.bagId = bagId;
        this.totalItem = totalItem;
        this.bagItem = bagItemDOList;
    }
}
