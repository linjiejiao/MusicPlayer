package cn.ljj.musicplayer.files;

public interface Defines {
	static final int ERROR_OTHER = 0;
	static final int ERROR_MALFORMEDURL = 1;
	static final int ERROR_PROTOCOL = 2;
	static final int ERROR_UNSUPPORTEDENCODING = 3;
	static final int ERROR_FILENOTFOUND = 4;
	static final int ERROR_IOEXCEPTION = 5;
	static final int ERROR_PARAMETER = 6;
	
	static final String BAIDU_QUERY_BASE = "http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.search.common&page_size=10&page_no=1&format=xml&from=ios&version=4.1.1&query=";

}
