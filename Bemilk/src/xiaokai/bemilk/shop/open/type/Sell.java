package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author Winfxk
 */
@SuppressWarnings({ "unchecked", "null" })
public class Sell extends BaseDis {
	/**
	 * 处理回收商店的数据
	 * 
	 * @param data
	 */
	public Sell(ShopData data) {
		super(data);
	}

	@Override
	public boolean MakeMain() {
		String[] myKey = { "{Player}", "{Items}", "{Money}", "{MyMoney}" };
		int MinCount = Tool.ObjectToInt(Item.get("MinCount"), 1);
		int MaxCount = Tool.ObjectToInt(Item.get("MaxCount"), 64);
		Object[] myData = { player.getName(), getItems(), Money, MyMoney() };
		String Contxt = msg.getSun("界面", "回收商店", "内容", myKey, myData);
		form.addLabel(Contxt);
		form.addSlider(msg.getSun("界面", "回收商店", "出售数量", k, d), MinCount, MaxCount, 1, MinCount);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		int Count = (int) data.getSliderResponse(1);
		boolean isNbt = Tool.ObjToBool(Item.get("isNbt"), false);
		Map<String, Object> items = (Map<String, Object>) Item.get("Items");
		Set<String> list = items.keySet();
		Map<Integer, cn.nukkit.item.Item> Contents = Inventory.getContents();
		Set<Integer> iSet = Contents.keySet();
		for (String ID : list) {
			int ItemCount = 0;
			cn.nukkit.item.Item item = Tool.loadItem((Map<String, Object>) items.get(ID));
			for (Integer ike : iSet) {
				cn.nukkit.item.Item item2 = Contents.get(ike);
				CompoundTag nbt1 = item.getNamedTag();
				CompoundTag nbt2 = item2.getNamedTag();
				nbt1 = nbt1 == null ? new CompoundTag() : nbt1;
				nbt2 = nbt2 == null ? new CompoundTag() : nbt2;
				if ((!isNbt || !(item == null || item2 == null) || nbt1 == nbt2 || nbt1.equals(nbt2))
						&& ItemID.getID(item).equals(ItemID.getID(item2)))
					ItemCount += item2.getCount();
			}
			if (ItemCount < (Count * item.getCount()))
				return Tip(msg.getSun("界面", "回收商店", "物品数量不足",
						new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}" },
						new Object[] { player.getName(), MyMoney(), ItemID.getName(item), ItemID.getID(item) }));
		}
		for (String ID : list) {
			cn.nukkit.item.Item item = Tool.loadItem((Map<String, Object>) items.get(ID));
			int ItemCount = Count * item.getCount();
			List<Integer> siSet = new ArrayList<>(Contents.keySet());
			for (int i = 0; i < siSet.size(); i++) {
				Integer ike = siSet.get(i);
				cn.nukkit.item.Item item2 = Contents.get(ike);
				CompoundTag nbt1 = item.getNamedTag();
				CompoundTag nbt2 = item2.getNamedTag();
				nbt1 = nbt1 == null ? new CompoundTag() : nbt1;
				nbt2 = nbt2 == null ? new CompoundTag() : nbt2;
				if ((!isNbt || !(item == null || item2 == null) || nbt1 == nbt2 || nbt1.equals(nbt2))
						&& ItemID.getID(item).equals(ItemID.getID(item2)))
					if (item2.getCount() < ItemCount) {
						ItemCount -= item2.getCount();
						Contents.remove(ike);
					} else if (item2.getCount() == ItemCount) {
						Contents.remove(ike);
						break;
					} else if (item2.getCount() > ItemCount) {
						item2.setCount(item2.getCount() - ItemCount);
						Contents.put(ike, item2);
						break;
					}
			}
			Inventory.setContents(Contents);
		}
		return send(msg.getSun("界面", "回收商店", "出售成功",
				new String[] { "{Player}", "{Money}", "{Items}", "{ItemCount}", "{addsMoney}", "{addMoney}" },
				new Object[] { player.getName(), MyMoney(), getItems(), Count, Count * Money,
						addMoney(Count * Money) }));
	}
}