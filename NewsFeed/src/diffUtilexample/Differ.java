package diffUtilexample;

import java.util.ArrayList;
import java.util.List;

import difflib.DiffUtils;
import difflib.Patch;
import difflib.*;

public class Differ {

	public Differ() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
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
		
		System.out.println(patch);
		for(Delta delta : patch.getDeltas())
		{
			System.out.println(delta);
			List<String> list = (List<String>) delta.getRevised().getLines();
			delta.getRevised().getLines();
		}
		System.out.println(patch);
		
	}

}
