package wayeet.ghidra.format.lx.datatype;

import ghidra.app.util.bin.StructConverter;
import ghidra.program.model.data.ArrayDataType;
import ghidra.program.model.data.StructureDataType;
import wayeet.ghidra.format.lx.model.Executable;

public class LoaderSectionType extends StructureDataType {

	public LoaderSectionType(Executable executable, int size) {
		super("IMAGE_LE_LOADER", 0);
		
		if (executable.objects.size() > 0) {
			add(
				new ArrayDataType(new ObjectMapEntryType() , executable.objects.size(), 0),
				"object_table",
				"Module Object Table. Entries are numbered starting from one."
			);
			
			var t = new LePageMapEntryType();
			for (var object : executable.objects) {
				if (object.pageCount > 0) {
					add(new ArrayDataType(t, object.pageCount, 0), "pagemap_obj" + object.number, "Page map table for object #" + object.number);	
				}
			}
		}
			
		var h = executable.header;
		if (h.resourceTableOffset > 0 && h.resourceCount > 0) {
			if (h.residentNameTableOffset - h.resourceTableOffset > 0) {
				add(new ArrayDataType(StructConverter.BYTE, h.residentNameTableOffset - h.resourceTableOffset, 0), "resources", "todo");	
			}	
		}
		if (h.entryTableOffset - h.residentNameTableOffset > 0) {
			add(new ArrayDataType(StructConverter.BYTE, h.entryTableOffset - h.residentNameTableOffset, 0), "name_table", "todo");
		}
		if (h.fixupPageTableOffset - h.entryTableOffset > 0) {
			add(new ArrayDataType(StructConverter.BYTE, h.fixupPageTableOffset - h.entryTableOffset, 0), "entry_table", "todo (len, ascci, ord) tuples");
		}
	}

}
