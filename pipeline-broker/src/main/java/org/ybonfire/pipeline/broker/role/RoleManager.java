package org.ybonfire.pipeline.broker.role;

import org.ybonfire.pipeline.broker.model.Role;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Role管理器
 *
 * @author Bo.Yuan5
 * @date 2022-09-02 15:47
 */
public final class RoleManager {
    private static final AtomicReference<Role> ROLE_HOLDER = new AtomicReference<>(Role.LEADER);

    /**
     * @description: 设置角色
     * @param:
     * @return:
     * @date: 2022/09/02 16:13:37
     */
    public static void set(final Role role) {
        ROLE_HOLDER.set(role);
    }

    /**
     * @description: 获取角色
     * @param:
     * @return:
     * @date: 2022/09/02 16:13:45
     */
    public static Role get() {
        return ROLE_HOLDER.get();
    }
}