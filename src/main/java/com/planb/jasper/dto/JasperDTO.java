package com.planb.jasper.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class JasperDTO {

    String storeCode;
    String item_plu_fleje;
    String itemShortName;
    String itemLongName;
    String item_price_fleje;
    String itemStockUm;
    double iteStockFactor;
    String itemPackUm;
    double itemPackFactor;
    String structureCode;
    String structureParentCode;
    String structureName;
    String paramValueString;
    String item_marca_fleje;
    String item_desc_fleje;
    String item_presentacion_2_fleje;
    String item_fecha_fleje;
    String item_valor_unidad_desc_fleje;



}

