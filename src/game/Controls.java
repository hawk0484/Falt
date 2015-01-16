package game;

import java.io.*;
import java.util.Arrays;

public class Controls {
	public static String[] ControlNames = {"Camera North","Camera South","Camera West","Camera East"};
	public static Key[] DefaultControls = {Key.n('w'),Key.n('s'),Key.n('a'),Key.n('d')};
	public static Key[] CurrentControls = null;
	
	public static void loadControls(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		CurrentControls = new Key[ControlNames.length];
		while ((line = br.readLine()) != null) {
			
			String[] Mex = line.split(":");
			int i = Arrays.asList(ControlNames).indexOf(Mex[0]);
			CurrentControls[i]=Key.n(
					Integer.valueOf(Mex[1])
					,Mex[2].toCharArray()[0]);
		}
		br.close();
	}
	public static void saveControls(File file) throws FileNotFoundException{
		
		PrintWriter pw = new PrintWriter(file);
		boolean flag0=false;
		int i=0;
		for(Key k : CurrentControls){
			if(flag0){
				pw.println("");
			}
			flag0=true;
			pw.print(ControlNames[i]+":"+k.keycode+":"+k.keychar);
			i++;
		}
		pw.flush();
		pw.close();
	}
	public static Key getControl(String name){
		return CurrentControls[(Arrays.asList(ControlNames).indexOf(name))];
	}
	public static void setDefaultControls(){
		CurrentControls=DefaultControls;
	}
}
