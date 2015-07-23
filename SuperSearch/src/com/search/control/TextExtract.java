package com.search.control;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 閸︺劎鍤庨幀褎妞傞梻鏉戝敶閹惰棄褰囨稉濠氼暯缁紮绱欓弬浼存閵嗕礁宕ョ�銏㈢搼閿涘缍夋い鐢垫畱濮濓絾鏋冮妴锟� * 闁插洨鏁ゆ禍锟絙>閸╄桨绨悰灞芥健閸掑棗绔烽崙鑺ユ殶</b>閻ㄥ嫭鏌熷▔鏇礉娑撹桨绻氶幐渚�拷閻€劍锟藉▽鈩冩箒闁藉牆顕悧鐟扮暰缂冩垹鐝紓鏍у晸鐟欏嫬鍨妴锟� * </p>
 * @author Chen Xin(xchen@ir.hit.edu.cn)
 * Created on 2009-1-11
 * Updated on 2010-08-09
 */
public class TextExtract {
	
	private List<String> lines;
	private final static int blocksWidth=3;
	private int threshold;
	private String html;
	private boolean flag;
	private int start;
	private int end;
	private StringBuilder text;
	private ArrayList<Integer> indexDistribution;
	
	public TextExtract() {
		lines = new ArrayList<String>();
		indexDistribution = new ArrayList<Integer>();
		text = new StringBuilder();
		flag = false;
		/*
		/* 瑜版挸绶熼幎钘夊絿閻ㄥ嫮缍夋い鍨劀閺傚洣鑵戦柆鍥у煂閹存劕娼￠惃鍕煀闂傜粯鐖ｆ０妯绘弓閸撴棃娅庨弮璁圭礉閸欘亣顩︽晶鐐层亣濮濄倝妲囬崐鐓庡祮閸欘垬锟�/
		/* 闂冨牆锟芥晶鐐层亣閿涘苯鍣涵顔惧芳閹绘劕宕岄敍灞藉将閸ョ偟宸兼稉瀣閿涙稑锟介崣妯虹毈閿涘苯娅旀竟棰佺窗婢堆嶇礉娴ｅ棗褰叉禒銉ょ箽鐠囦焦濞婇崚鏉垮涧閺堝绔撮崣銉ㄧ樈閻ㄥ嫭顒滈弬锟�/
		*/
		threshold	= -1;   
	}
	

	/**
	 * 閹惰棄褰囩純鎴︺�濮濓絾鏋冮敍灞肩瑝閸掋倖鏌囩拠銉х秹妞ゅ灚妲搁崥锔芥Ц閻╊喖缍嶉崹瀣拷閸楀啿鍑￠惌銉ょ炊閸忋儳娈戦懖顖氱暰閺勵垰褰叉禒銉﹀▕閸欐牗顒滈弬鍥╂畱娑撳顣界猾鑽ょ秹妞ょ偣锟�
	 * 
	 * @param _html 缂冩垿銆塇TML鐎涙顑佹稉锟�	 * 
	 * @return 缂冩垿銆夊锝嗘瀮string
	 */
	public String parse(String _html) {
		return parse(_html, false);
	}
	
	/**
	 * 閸掋倖鏌囨导鐘插弳HTML閿涘矁瀚㈤弰顖欏瘜妫版琚純鎴︺�閿涘苯鍨幎钘夊絿濮濓絾鏋冮敍娑樻儊閸掓瑨绶崙锟絙>"unkown"</b>閵嗭拷
	 * 
	 * @param _html 缂冩垿銆塇TML鐎涙顑佹稉锟�	 * @param _flag true鏉╂稖顢戞稉濠氼暯缁鍨介弬锟�閻胶鏆愬銈呭棘閺佹澘鍨妯款吇娑撶alse
	 * 
	 * @return 缂冩垿銆夊锝嗘瀮string
	 */
	public String parse(String _html, boolean _flag) {
		flag = _flag;
		html = _html;
		html = preProcess(html);
//		System.out.println(html);
		return getText();
	}
	private static int FREQUENT_URL = 30;
	private static Pattern links = Pattern.compile("<[aA]\\s+[Hh][Rr][Ee][Ff]=[\"|\']?([^>\"\' ]+)[\"|\']?\\s*[^>]*>([^>]+)</a>(\\s*.{0,"+FREQUENT_URL+"}\\s*<a\\s+href=[\"|\']?([^>\"\' ]+)[\"|\']?\\s*[^>]*>([^>]+)</[aA]>){2,100}", Pattern.DOTALL);
	public static String preProcess(String source) {
		
		source = source.replaceAll("(?is)<!DOCTYPE.*?>", "");
		source = source.replaceAll("(?is)<!--.*?-->", "");				// remove html comment
		source = source.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
		source = source.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
		source = source.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
		
		//閸撴棃娅庢潻鐐电敾閹存劗澧栭惃鍕Т闁剧偓甯撮弬鍥ㄦ拱閿涘牐顓绘稉鐑樻Ц閿涘苯绠嶉崨濠冨灗閸ｎ亪鐓堕敍锟界搾鍛存懠閹恒儱顦块挊蹇庣艾span娑擄拷
		source = source.replaceAll("<[sS][pP][aA][nN].*?>", "");
		source = source.replaceAll("</[sS][pP][aA][nN]>", "");

		int len = source.length();
		while ((source = links.matcher(source).replaceAll("")).length() != len)
		{
			len = source.length();
		}
			;//continue;
		
		//source = links.matcher(source).replaceAll("");
		
		//闂冨弶顒沨tml娑擃厼婀�>娑擃厼瀵橀幏顒�亣娴滃骸褰块惃鍕灲閺傦拷
		source = source.replaceAll("<[^>'\"]*['\"].*['\"].*?>", "");

		source = source.replaceAll("<.*?>", "");
		source = source.replaceAll("<.*?>", "");
		source = source.replaceAll("\r\n", "\n");

		return source;
	
	}
	
	private String getText() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		int empty = 0;//缁岄缚顢戦惃鍕殶闁诧拷
		for (int i = 0; i < lines.size() - blocksWidth; i++) {
			
			if (lines.get(i).length() == 0)
			{
				empty++;
			}
			
			int wordsNum = 0;
			for (int j = i; j < i + blocksWidth; j++) { 
				lines.set(j, lines.get(j).replaceAll("\\s+", ""));
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
			//System.out.println(wordsNum);
		}
		int sum = 0;

		for (int i=0; i< indexDistribution.size(); i++)
		{
			sum += indexDistribution.get(i);
		}
		
		threshold = Math.min(100, (sum/indexDistribution.size())<<(empty/(lines.size()-empty)>>>1));
		threshold = Math.max(50, threshold);
		
		start = -1; end = -1;
		boolean boolstart = false, boolend = false;
		boolean firstMatch = true;//閸撳秹娼伴惃鍕垼妫版ê娼″锟界窔濮ｆ棁绶濈亸蹇ョ礉鎼存棁顕氶崙蹇撶毈娑撳骸鐣犻崠褰掑帳閻ㄥ嫰妲囬崐锟�		text.setLength(0);
		
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < indexDistribution.size() - 1; i++) {
			
			if(firstMatch && ! boolstart)
			{
				if (indexDistribution.get(i) > (threshold/2) && ! boolstart) {
					if (indexDistribution.get(i+1).intValue() != 0 
						|| indexDistribution.get(i+2).intValue() != 0) {
						firstMatch = false;
						boolstart = true;
						start = i;
						continue;
					}
				}
				
			}
			if (indexDistribution.get(i) > threshold && ! boolstart) {
				if (indexDistribution.get(i+1).intValue() != 0 
					|| indexDistribution.get(i+2).intValue() != 0
					|| indexDistribution.get(i+3).intValue() != 0) {
					boolstart = true;
					start = i;
					continue;
				}
			}
			if (boolstart) {
				if (indexDistribution.get(i).intValue() == 0 
					|| indexDistribution.get(i+1).intValue() == 0) {
					end = i;
					boolend = true;
				}
			}
		
			if (boolend) {
				buffer.setLength(0);
				//System.out.println(start+1 + "\t\t" + end+1);
				for (int ii = start; ii <= end; ii++) {
					if (lines.get(ii).length() < 5) continue;
					buffer.append(lines.get(ii) + "\n");
				}
				String str = buffer.toString();
				//System.out.println(str);
				if (str.contains("Copyright")  || str.contains("閻楀牊娼堥幍锟芥箒") ) continue; 
				text.append(str);
				boolstart = boolend = false;
			}
		}
		
		if (start > end)
		{
			buffer.setLength(0);
			int size_1 = lines.size()-1;
			for (int ii = start; ii <= size_1; ii++) {
				if (lines.get(ii).length() < 5) continue;
				buffer.append(lines.get(ii) + "\n");
			}
			String str = buffer.toString();
			//System.out.println(str);
			if ((!str.contains("Copyright"))  || (!str.contains("閻楀牊娼堥幍锟芥箒")) ) 
			{	
				text.append(str);
			}
		}
		
		return text.toString();
	}
	
	public static void main(String[] args) throws IOException
	{
		System.out.println("===============");
		File file = new File( "E:\\course\\eclipse-workspace\\Data\\test.html");
		FileInputStream in = new FileInputStream(file);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuffer sbStr = new StringBuffer();
		String s = "http://blog.sina.com.cn/s/blog_4c687df40102e231.html?tj=1";
		while((s=reader.readLine())!=null)
		{
			sbStr.append(s);
			sbStr.append("\r\n");
		}
		
		reader.close();
		String string=sbStr.toString();
		
		System.out.print(string);
		
		//source = source.replaceAll("<[^'\"]*['\"].*['\"].*?>", "");
System.out.println(TextExtract.preProcess(string));

		
	}
}