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
    public class Browse implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 浏览记录id
            */
            @TableId(value = "browseId", type = IdType.AUTO)
    private Integer browseId;

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
            * 浏览时间
            */
        @TableField("browseTime")
    private Date browseTime;


}
