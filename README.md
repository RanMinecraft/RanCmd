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

## 安装

1. 从 [Releases](https://www.ranmc.cc/) 下载最新版本的 `RanCmd.jar`
2. 将 `RanCmd.jar` 放入服务端的 `plugins/` 目录
3. 重启服务器或使用 `/reload` 加载插件
4. 享受使用 🎉

## 构建

### 前置要求

- JDK 26+
- Maven 3.8+

### 构建步骤

```bash
git clone https://github.com/your-username/RanCmd.git
cd RanCmd
mvn clean install
```

构建产物会自动输出到 `~/Desktop/Paper/plugins/` 目录（本地测试服务器目录）。

## 配置文件

`config.yml`：

```yaml
# 无需配置
```

插件**开箱即用**，无需任何配置。

## 兼容性

- Minecraft 1.13+（API 版本）
- 支持 Paper、Spigot、Purpur 等下游服务端

## 数据统计

[![bStats](https://bstats.org/signatures/bukkit/RanCmd.svg)](https://bstats.org/plugin/bukkit/RanCmd/28165)