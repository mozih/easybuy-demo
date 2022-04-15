package com.mo.easybuy.pojo;

    import java.math.BigDecimal;
    import com.baomidou.mybatisplus.annotation.IdType;
    import java.util.Date;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableField;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author mo
* @since 2022-03-03
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 价格id
            */
            @TableId(value = "priceId", type = IdType.AUTO)
    private Integer priceId;

            /**
            * 商品id
            */
        @TableField("comId")
    private Integer comId;

            /**
            * 当前价格
            */
        @TableField("priceNow")
    private BigDecimal priceNow;

            /**
            * 价格采集时间
            */
        @TableField("priceTime")
    private Date priceTime;


}
