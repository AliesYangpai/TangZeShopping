package org.tangze.work.Test;

/**
 * Created by Administrator on 2016/9/29 0029.
 * 类描述
 * 版本
 */
public class GridItem {

    /**
     * 全部分类、热门分类等....
     */
    private String bigClassifyText;

    private String smallClassifyText;


    /**
     * 全部分类、热门分类所在的section
     */
    private int section;


    public GridItem() {
    }

    public GridItem(String bigClassifyText, String smallClassifyText, int section) {
        this.bigClassifyText = bigClassifyText;
        this.smallClassifyText = smallClassifyText;
        this.section = section;
    }


    public String getBigClassifyText() {
        return bigClassifyText;
    }

    public void setBigClassifyText(String bigClassifyText) {
        this.bigClassifyText = bigClassifyText;
    }

    public String getSmallClassifyText() {
        return smallClassifyText;
    }

    public void setSmallClassifyText(String smallClassifyText) {
        this.smallClassifyText = smallClassifyText;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }
}
