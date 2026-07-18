# RanCmd

一个轻量级的 Minecraft Paper/Bukkit 服务端插件，允许管理员**以其他玩家的身份执行指令**。

---

## 功能

- **模拟玩家执行指令** — 以指定玩家的身份运行任意指令
- **临时 OP 执行** — 临时授予玩家 OP 权限执行指令，执行后自动收回
- **零配置** — 开箱即用，无需任何配置
- **轻量** — 仅 ~180 行代码，无外部依赖

## 指令

| 指令 | 权限 | 描述 |
|------|------|------|
| `/rancmd reload` | `rancmd.admin` | 重载插件配置 |
| `/rancmd help` | `rancmd.admin` | 查看帮助信息 |
| `/rancmd cmd <玩家> <指令>` | `rancmd.admin` | 指定玩家执行指令 |
| `/rancmd opcmd <玩家> <指令>` | `rancmd.admin` | 指定玩家以 OP 权限执行指令 |

> 执行指令时**不需要**在指令前加斜杠 `/`。

## 权限

| 权限节点 | 默认 | 描述 |
|----------|------|------|
| `rancmd.admin` | OP | 允许使用所有 `/rancmd` 子指令 |
| `rancmd.user` | 所有人 | (预留) |

## 兼容性

- JDK 26 +
- Minecraft 1.13+（API 版本）
- 支持 Paper、Spigot、Purpur 等下游服务端

## 数据统计

[![bStats](https://bstats.org/signatures/bukkit/RanCmd.svg)](https://bstats.org/plugin/bukkit/RanCmd/28165)