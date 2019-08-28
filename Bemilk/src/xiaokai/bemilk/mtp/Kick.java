package xiaokai.bemilk.mtp;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import xiaokai.bemilk.Bemilk;
import xiaokai.bemilk.data.FormID;
import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.tool.data.ItemID;

/**
 * @author Winfxk
 */
public class Kick {
	public static Kick kick;
	/**
	 * 插件主累对象
	 */
	public Bemilk mis;
	/**
	 * 插件猪被子文件 <b>config</b></br>
	 * 表单ID配置文件 <b>formIdConfig</b> </br>
	 * 商店配置文件 <b>ShopConfig</b>
	 */
	public Config config, formIdConfig, ShopConfig;
	/**
	 * 系统配置文件的文件名
	 */
	public String ConfigName = "Config.yml";
	/**
	 * 商店配置文件名
	 */
	public String ShopConfigName = "Shop.yml";
	/**
	 * 表单ID存储类
	 */
	public FormID formID;
	/**
	 * 消息文件存储文件名
	 */
	public String MsgName = "Message.yml";
	/**
	 * 消息文件类
	 */
	public Message Message;
	/**
	 * 要初始化的表单ID键值
	 */
	public String[] FormIDName = { /* 0 */ "主页", /* 1 */"更多设置", /* 2 */"商店分页", /* 3 */"添加分页", /* 4 */ "修改分页",
			/* 5 */ "删除分页", /* 6 */"删除分页确认", /* 7 */"出售回收添加商店项目选择添加方式页", /* 8 */"管理员选择的是从背包选择物品，列出的物品列表界面",
			/* 9 */"管理员选择从背包选择物品，选择后输入上架数量的界面", /* 10 */"选择物品完毕，要求玩家输入项目价格的界面", /* 11 */"管理员添加出售回收类型的商店手动输入物品数据的界面",
			/* 12 */"物品兑换上架手动输入数据页面", /* 13 */"物品兑换上架从背包选择物品的物品列表页面", /* 14 */"物品兑换物品界面给玩家设置要添加的物品的数量的界面",
			/* 15 */"在添加物品兑换物品项目的时候物品选择完毕，设置物品数量的界面", /* 16 */"再添加附魔商店的时候让选择附魔方式的界面", /* 17 */"添加附魔商店的时候输入附魔数据的界面",
			/* 18 */"添加物品修复商店项目时的添加类型界面", /* 19 */"再添加工具修复商店项目的时候输入数据的界面", /* 20 */"添加商店时显示能添加的项目列表页",
			/* 21 */"物品兑换上架从背包选择物品的物品列表备用页面", /* 22 */"商店删除项目页面显示列表", /* 23 */"商店删除项目确认界面",
			/* 24 */"创建个人商店让选择添加物品方式的页面", /* 25 */"创建个人商店并且是以手动输入数据的页面", /* 26 */"创建个人商店并且是以从背包获取物品数据的页面" };
	/**
	 * 表单ID存储位置
	 */
	public String FormIDConfigName = "FormID.yml";
	/**
	 * 用户存储自定义ID名称的地方
	 */
	public String ItemIDConfigName = "ItemID.yml";
	/**
	 * 玩家数据库
	 */
	public LinkedHashMap<String, MyPlayer> PlayerDataMap = new LinkedHashMap<>();
	/**
	 * 要检查默认设置的配置文件
	 */
	public String[] LoadFileName = { ConfigName, ShopConfigName, MsgName };
	/**
	 * 要检查数据是否匹配的配置文件
	 */
	public String[] isLoadFileName = { ConfigName, MsgName };
	/**
	 * 过滤玩家进入的模式
	 */
	public static final String[] FilteredModel = { "无", "白名单", "黑名单" };
	/**
	 * 过滤的权限
	 */
	public static final String[] FilterPermissions = { "无", "服务器管理员", "普通玩家" };
	/**
	 * 按钮显示的贴图类型
	 */
	public static final String[] IconType = { "无图标", "本地资源", "网络资源" };
	/**
	 * 能创建的商店按钮的类型
	 */
	public static final String[] ButtonTypeList = { "物品出售", "物品回收", "物品兑换", "物品附魔", "工具修复" };
	/**
	 * 添加的商店的配置文件存储位置
	 */
	public static final String ShopConfigPath = "Shop/";
	/**
	 * 玩家配置文件存储路径
	 */
	public static final String PlayerConfigPath = "Players/";
	/**
	 * 在启动服务器时检查文件夹是否创建，要检查的列表
	 */
	public static final String[] LoadDirList = { ShopConfigPath, PlayerConfigPath };
	/**
	 * 异步线程类
	 */
	public startThread sThread;
	/**
	 * 创建项目的时候添加物品的方式
	 */
	public static final String[] addShopType = { "从背包选择物品", "手动输入数据" };
	/**
	 * 创建项目时附魔的方式
	 */
	public static final String[] addItemEnchantType = { "自定义概率", "定级概率", "定级出售" };
	/**
	 * 创建项目时的耐久方式
	 */
	public static final String[] addItemRepairType = { "定量增加", "总量增加", "随机增加" };
	/**
	 * 个人商店的类型
	 */
	public static final String[] addMyShopType = { "出售", "回收" };

	public Kick(Bemilk knickers) {
		kick = this;
		if (!knickers.getDataFolder().exists())
			knickers.getDataFolder().mkdirs();
		mis = knickers;
		formIdConfig = new Config(new File(knickers.getDataFolder(), FormIDConfigName), Config.YAML);
		(new Belle(this)).start();
		config = new Config(new File(knickers.getDataFolder(), ConfigName), Config.YAML);
		ShopConfig = new Config(new File(mis.getDataFolder(), ShopConfigName), Config.YAML);
		formID = new FormID(this);
		formID.setConfig(formIdConfig.getAll());
		Message = new Message(this);
		mis.getLogger().info("§6已加载§9" + ItemID.load() + "§6个物品数据~");
		sThread = new startThread(this);
		sThread.start();
	}

	/**
	 * 判断一个沙雕是不是管理员
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAdmin(CommandSender player) {
		if (!player.isPlayer())
			return true;
		return isAdmin((Player) player);
	}

	/**
	 * 判断一个沙雕是不是管理员
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAdmin(Player player) {
		if (player == null)
			return false;
		if (!player.isPlayer())
			return true;
		if (kick.config.getBoolean("管理员白名单")) {
			return kick.config.getList("管理员").contains(player.getName());
		} else
			return player.isOp();
	}
}
