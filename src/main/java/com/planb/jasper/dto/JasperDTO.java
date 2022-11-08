package com.planb.jasper.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class JasperDTO {

    String storeCode;
    String itemPlu;
    String itemShortName;
    String itemLongName;
    double itemPriceValue;
    String itemStockUm;
    double iteStockFactor;
    String itemPackUm;
    double itemPackFactor;
    String structureCode;
    String structureParentCode;
    String structureName;
    String paramValueString;
}

