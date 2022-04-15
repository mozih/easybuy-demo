package com.mo.easybuy.pojo;

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
    public class Mark implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 评价id
            */
            @TableId(value = "markId", type = IdType.AUTO)
    private Integer markId;

            /**
            * 用户id
            */
        @TableField("userId")
    private Integer userId;

            /**
            * 商品id
            */
        @TableField("comId")
    private Integer comId;

            /**
            * 评分
            */
        @TableField("markScore")
    private Integer markScore;

            /**
            * 评价内容
            */
        @TableField("markContent")
    private String markContent;

            /**
            * 评价时间
            */
        @TableField("markTime")
    private Date markTime;


}
