package com.lokesh.poc.bag.dataobject.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private String bagId;
    private int totalItem;
    private List<BagItemDO> bagItem;

    public BagDO(String bagId, List<BagItemDO> bagItemDOList) {
        this.bagId = bagId;
        this.bagItem = bagItemDOList;
    }
}
