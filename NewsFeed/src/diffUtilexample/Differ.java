package diffUtilexample;

import java.util.ArrayList;
import java.util.List;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.*;
import difflib.Delta.TYPE;

public class Differ {

	private List<String> insert;
	private List<String> delete;
	private List<String> change;
	
	@SuppressWarnings("unchecked")
	public Differ(List<String> file1, List<String> file2) {
		
		Patch patch = DiffUtils.diff(file1, file2);
		
		insert = new ArrayList<String>();
		delete = new ArrayList<String>();
		change = new ArrayList<String>();
		
		for(Delta delta : patch.getDeltas())
		{
			TYPE type = delta.getType();
			if(type == TYPE.INSERT) insert.addAll((List<String>) delta.getRevised().getLines());
			if(type == TYPE.DELETE) delete.addAll((List<String>) delta.getOriginal().getLines());
			if(type == TYPE.CHANGE) change.addAll((List<String>) delta.getOriginal().getLines());
		}
	}

	public List<String> getInsert(){
		
		return insert;
	}
	
	public List<String> getDelete(){
		
		return delete;
	}
	
	public List<String> getChange(){
		
		return change;
	}
}	
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> file1 = new ArrayList<String>();
		file1.add("3");
		file1.add("4");
		file1.add("5");
		file1.add("6");
		List<String> file2 = new ArrayList<String>();
		file2.add("1");
		file2.add("2");
		file2.add("3");
		file2.add("4");
		
		
		Patch patch = DiffUtils.diff(file1, file2);
		List<String> insert = null;
		List<String> delete = null;
		for(Delta delta : patch.getDeltas())
		{
			TYPE type = delta.getType();
			if(type == TYPE.INSERT) insert = (List<String>) delta.getRevised().getLines();
			if(type == TYPE.DELETE) delete = (List<String>) delta.getOriginal().getLines();
		}
		
		System.out.println(insert);
		System.out.println(delete);
	}*/