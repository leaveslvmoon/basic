package io.itman.model;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;


/**
 *
 *
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _JFinalDemoGenerator {

    public static DataSource getDataSource() {
        DruidPlugin druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1:3306/bbs?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull", "root", "Leaves@1992");
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }

    public static void main(String[] args) {
        // base model 所使用的包名
        String baseModelPackageName = "io.itman.model.base";
        // base model 文件保存路径
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/io/itman/model/base";

        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "io.itman.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";

        // 创建生成器
        Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(false);
        // 添加不需要生成的表名
        generator.addExcludedTable("t_test");
        // 设置是否在 Model 中生成 dao 对象
        generator.setGenerateDaoInModel(true);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(true);
        // 设置是否生成字典文件
        generator.setGenerateDataDictionary(false);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        generator.setRemovedTableNamePrefixes("bbs_");
        // 生成
        generator.generate();

    }
}
