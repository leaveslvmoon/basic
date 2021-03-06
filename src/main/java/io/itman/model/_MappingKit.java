package io.itman.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("api_categories", "id", ApiCategories.class);
		arp.addMapping("api_content", "id", ApiContent.class);
		arp.addMapping("content", "id", Content.class);
		arp.addMapping("contentcategories", "id", Contentcategories.class);
		arp.addMapping("document_categories", "id", DocumentCategories.class);
		arp.addMapping("document_content", "id", DocumentContent.class);
		arp.addMapping("errorlog", "id", Errorlog.class);
		arp.addMapping("ip_whitelist", "id", IpWhitelist.class);
		arp.addMapping("log", "id", Log.class);
		arp.addMapping("mac_whitelist", "id", MacWhitelist.class);
		arp.addMapping("provincecitycounty", "id", Provincecitycounty.class);
		arp.addMapping("sys_administrator", "id", SysAdministrator.class);
		arp.addMapping("sys_appversion", "id", SysAppversion.class);
		arp.addMapping("sys_basicinfo", "id", SysBasicinfo.class);
		arp.addMapping("sys_button", "id", SysButton.class);
		arp.addMapping("sys_connection", "id", SysConnection.class);
		arp.addMapping("sys_job", "id", SysJob.class);
		arp.addMapping("sys_menu", "id", SysMenu.class);
		arp.addMapping("sys_menubutton", "id", SysMenubutton.class);
		arp.addMapping("sys_role", "id", SysRole.class);
		arp.addMapping("sys_rolemenu", "id", SysRolemenu.class);
		arp.addMapping("sys_smstemplate", "id", SysSmstemplate.class);
		arp.addMapping("sys_sqlmodel", "id", SysSqlmodel.class);
	}
}

