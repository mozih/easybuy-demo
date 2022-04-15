package com.mo.easybuy.pojo;

    import com.baomidou.mybatisplus.annotation.FieldFill;
    import com.baomidou.mybatisplus.annotation.IdType;
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
    public class Commodity implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "comId", type = IdType.AUTO)
    private Integer comId;

            /**
            * 商品名
            */
        @TableField("comName")
    private String comName;

            /**
            * 商品描述
            */
        @TableField("comDetail")
    private String comDetail;

            /**
            * 商品来源
            */
        @TableField("comAddress")
    private String comAddress;

            /**
            * 商品链接
            */
        @TableField("comUrl")
    private String comUrl;

    /**
     * 图片url
     */
    @TableField("comPicUrl")
    private String comPicUrl;
    /**
     * 浏览次数
     */
    @TableField(value = "comScan",fill = FieldFill.INSERT)
    private Long comScan;

    /**
     * 商品评价数
     * fill属性，插入时填充字段
     */
    @TableField(value = "comMarknumber",fill = FieldFill.INSERT)
    private Long comMarknumber;


}
