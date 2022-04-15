package com.mo.easybuy.pojo;

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
    public class User implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 用户id
            */
            @TableId(value = "userId", type = IdType.AUTO)
    private Integer userId;

            /**
            * 用户名
            */
        @TableField("userName")
    private String userName;

            /**
            * 密码
            */
        @TableField("userPassword")
    private String userPassword;

            /**
            * 邮箱
            */
    private String email;


}
