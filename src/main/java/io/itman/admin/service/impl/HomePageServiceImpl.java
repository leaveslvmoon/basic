package io.itman.admin.service.impl;


import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.service.HomePageService;
import io.itman.library.util.MathUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HomePageServiceImpl implements HomePageService {

    @Override
    public Map homePageData() {

        Map returnMap = new HashMap();
        try {
            Record todayMember = Db.findFirst("select IFNULL(count(1),0) as count from member where TO_DAYS(addTime) = TO_DAYS(NOW())");//今日新增会员
            Record allMember = Db.findFirst("select IFNULL(count(1),0) as count from member");//总会员数
            Record todayViewed = Db.findFirst("select IFNULL(sum(viewed),0) as viewed from promotiondatastatistics where TO_DAYS(accessTime) = TO_DAYS(NOW())");//今日浏览量

            Record weekMember = Db.findFirst("select IFNULL(count(1),0) as count from member where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(addTime)");//本周新增会员
            Record allWeekMember = Db.findFirst("select IFNULL(count(1),0) as count from member where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(addTime)");//本周总会员数
            Record weekClick = Db.findFirst("SELECT count(1) AS count FROM clickcount WHERE DATE_SUB( CURDATE( ), INTERVAL 7 DAY ) <= date( clickTime )");//本周点击量
            Record todayClick = Db.findFirst("SELECT count(1) AS count FROM clickcount WHERE TO_DAYS(clickTime) = TO_DAYS(NOW())");//今日点击量
            Record allClick = Db.findFirst("SELECT count(1) AS count FROM clickcount");//总点击量
            Record allProduct = Db.findFirst("select \n" +
                    "(select count(1) as count from loanbank) as loanbank,\n" +
                    "(select count(1) as count from loanonline) as loanonline");
            Integer allProducts = allProduct.getInt("loanbank")+allProduct.getInt("loanonline");//平台产品数
            List<Record> memberList = Db.find("SELECT SUBSTRING(from_days(TO_DAYS(addTime)),-5) AS `time`,COUNT(TO_DAYS(addTime)) as num  " +
                    " FROM `member` WHERE  DATE_SUB(CURDATE(), INTERVAL 9 DAY) <= date(addTime) GROUP BY  SUBSTRING(from_days(TO_DAYS(addTime)),-5)");//用户折线图
            List<Record> clickList = Db.find("SELECT SUBSTRING(from_days(TO_DAYS(clickTime)),-5) AS `time`,COUNT(TO_DAYS(clickTime)) as num  " +
                    " FROM `clickcount` WHERE  DATE_SUB(CURDATE(), INTERVAL 9 DAY) <= date(clickTime) GROUP BY  SUBSTRING(from_days(TO_DAYS(clickTime)),-5)");
            returnMap.put("todayMember",todayMember.getInt("count"));
            returnMap.put("allMember",allMember.getInt("count"));
            returnMap.put("todayViewed",todayViewed.getInt("viewed"));

            returnMap.put("weekMember",weekMember.getInt("count"));
            returnMap.put("allWeekMember",allWeekMember.getInt("count"));
            returnMap.put("weekClick",weekClick.getInt("count"));
            returnMap.put("todayClick",todayClick.getInt("count"));
            returnMap.put("allClick",allClick.getInt("count"));
            returnMap.put("todayClickRate", MathUtil.mul2(MathUtil.div(todayClick.getInt("count"),allClick.getInt("count"),2),100d,2));//今日点击率
            returnMap.put("weekClickRate",MathUtil.mul2(MathUtil.div(weekClick.getInt("count"),allClick.getInt("count"),2),100d,2));////本周点击率

            returnMap.put("allProducts",allProducts);
            String[] pastDateArray = new String[10];//时间-X轴
            Double[] memberArray = new Double[10];
            Double[] viewArray = new Double[10];
            for (int i = 0; i < 10; i++) {
                String pastDate = getPastDate(i);
                pastDateArray[i] = pastDate;
            }
            Arrays.sort(pastDateArray, 0, 10);

            for (int i = 0; i < 10; i++) {
                String pastDate = pastDateArray[i];
                for (Record record : memberList) {
                    if (record.get("time") != null && record.get("time").equals(pastDate)) {
                        memberArray[i] = Double.valueOf(record.get("num").toString());
                        break;
                    } else {
                        memberArray[i] = 0d;
                    }
                }
                for (Record record : clickList) {
                    if (record.get("time") != null && record.get("time").equals(pastDate)) {
                        viewArray[i] = Double.valueOf(record.get("num").toString());
                        break;
                    } else {
                        viewArray[i] = 0d;
                    }
                }
            }
            returnMap.put("pastDateArray", pastDateArray);//时间-X轴
            returnMap.put("memberArray", memberArray);
            returnMap.put("viewArray", viewArray);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    private String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String result = format.format(today);
        return result;
    }
}
