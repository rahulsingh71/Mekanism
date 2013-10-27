package mekanism.common.transporter;

import java.util.ArrayList;

import mekanism.api.EnumColor;
import mekanism.common.util.TransporterUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public abstract class TransporterFilter 
{
	public EnumColor color;
	
	public abstract boolean canFilter(ItemStack itemStack);
	
	public void write(NBTTagCompound nbtTags)
	{
		nbtTags.setInteger("color", TransporterUtils.colors.indexOf(color));
	}
	
	protected void read(NBTTagCompound nbtTags)
	{
		color = TransporterUtils.colors.get(nbtTags.getInteger("color"));
	}
	
	public void write(ArrayList data)
	{
		data.add(TransporterUtils.colors.indexOf(color));
	}
	
	protected void read(ByteArrayDataInput dataStream)
	{
		color = TransporterUtils.colors.get(dataStream.readInt());
	}
	
	public static TransporterFilter readFromNBT(NBTTagCompound nbtTags)
	{
		int type = nbtTags.getInteger("type");
		
		TransporterFilter filter = null;
		
		if(type == 0)
		{
			filter = new ItemStackFilter();
		}
		else {
			filter = new OreDictFilter();
		}
		
		filter.read(nbtTags);
		
		return filter;
	}
	
	public static TransporterFilter readFromPacket(ByteArrayDataInput dataStream)
	{
		int type = dataStream.readInt();
		
		TransporterFilter filter = null;
		
		if(type == 0)
		{
			filter = new ItemStackFilter();
		}
		else if(type == 1)
		{
			filter = new OreDictFilter();
		}
		
		filter.read(dataStream);
		
		return filter;
	}
	
	@Override
	public int hashCode() 
	{
		int code = 1;
		code = 31 * code + color.ordinal();
		return code;
	}
	
	@Override
	public boolean equals(Object filter)
	{
		return filter instanceof TransporterFilter && ((TransporterFilter)filter).color == color;
	}
}