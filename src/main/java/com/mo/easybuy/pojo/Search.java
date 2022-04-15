package com.mo.easybuy.pojo;

    import com.baomidou.mybatisplus.annotation.IdType;
    import java.util.Date;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableField;
    import java.io.Serializable;

    import com.baomidou.mybatisplus.annotation.TableLogic;
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
    public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 搜索记录id
            */
            @TableId(value = "searchId", type = IdType.AUTO)
    private Integer searchId;

            /**
            * 用户id
            */
        @TableField("userId")
    private Integer userId;

            /**
            * 搜索词
            */
        @TableField("searchContent")
    private String searchContent;

            /**
            * 搜索时间
            */
        @TableField("searchTime")
    private Date searchTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer deleted;
}
