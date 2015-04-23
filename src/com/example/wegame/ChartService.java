package com.example.wegame;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ChartService {

    private GraphicalView mGraphicalView;
    private XYMultipleSeriesDataset multipleSeriesDataset;// ���ݼ�����
    private XYMultipleSeriesRenderer multipleSeriesRenderer;// ��Ⱦ������
    private XYSeries mSeries;// �����������ݼ�
    private XYSeriesRenderer mRenderer;// ����������Ⱦ��
    private Context context;

    public ChartService(Context context) {
        this.context = context;
    }

    /**
     * ��ȡͼ��
     * 
     * @return
     */
    public GraphicalView getGraphicalView() {
        mGraphicalView = ChartFactory.getCubeLineChartView(context,
                multipleSeriesDataset, multipleSeriesRenderer, 0.1f);
        return mGraphicalView;
    }

    /**
     * ��ȡ���ݼ�����xy����ļ���
     * 
     * @param curveTitle
     */
    public void setXYMultipleSeriesDataset(String curveTitle) {
        multipleSeriesDataset = new XYMultipleSeriesDataset();
        mSeries = new XYSeries(curveTitle);
        multipleSeriesDataset.addSeries(mSeries);
    }

    /**
     * ��ȡ��Ⱦ��
     * 
     * @param maxX
     *            x�����ֵ
     * @param maxY
     *            y�����ֵ
     * @param chartTitle
     *            ���ߵı���
     * @param xTitle
     *            x�����
     * @param yTitle
     *            y�����
     * @param axeColor
     *            ��������ɫ
     * @param labelColor
     *            ������ɫ
     * @param curveColor
     *            ������ɫ
     * @param gridColor
     *            ������ɫ
     */
    public void setXYMultipleSeriesRenderer(double maxX, double maxY,
            String chartTitle, String xTitle, String yTitle, int axeColor,
            int labelColor, int curveColor, int gridColor) {
        multipleSeriesRenderer = new XYMultipleSeriesRenderer();
        if (chartTitle != null) {
            multipleSeriesRenderer.setChartTitle(chartTitle);
        }
        multipleSeriesRenderer.setXTitle(xTitle);
        multipleSeriesRenderer.setYTitle(yTitle);
        multipleSeriesRenderer.setRange(new double[] { 0, maxX, 0, maxY });//xy��ķ�Χ
        multipleSeriesRenderer.setLabelsColor(labelColor);
        multipleSeriesRenderer.setXLabels(10);
        multipleSeriesRenderer.setYLabels(10);
        multipleSeriesRenderer.setXLabelsAlign(Align.RIGHT);
        multipleSeriesRenderer.setYLabelsAlign(Align.RIGHT);
        multipleSeriesRenderer.setAxisTitleTextSize(20);
        multipleSeriesRenderer.setChartTitleTextSize(20);
        multipleSeriesRenderer.setLabelsTextSize(20);
        multipleSeriesRenderer.setLegendTextSize(20);
        multipleSeriesRenderer.setPointSize(2f);//�������ߴ�
        multipleSeriesRenderer.setFitLegend(true);
        multipleSeriesRenderer.setMargins(new int[] { 20, 30, 15, 20 });
        multipleSeriesRenderer.setShowGrid(true);
        multipleSeriesRenderer.setZoomEnabled(true, false);
        multipleSeriesRenderer.setAxesColor(axeColor);
        multipleSeriesRenderer.setGridColor(gridColor);
        multipleSeriesRenderer.setBackgroundColor(Color.WHITE);//����ɫ
        multipleSeriesRenderer.setMarginsColor(Color.WHITE);//�߾౳��ɫ��Ĭ�ϱ���ɫΪ��ɫ�������޸�Ϊ��ɫ
        mRenderer = new XYSeriesRenderer();
        mRenderer.setColor(curveColor);
        mRenderer.setPointStyle(PointStyle.CIRCLE);//����񣬿���ΪԲ�㣬���ε�ȵ�
        multipleSeriesRenderer.addSeriesRenderer(mRenderer);
    }

    /**
     * �����¼ӵ����ݣ��������ߣ�ֻ�����������߳�
     * 
     * @param x
     *            �¼ӵ��x����
     * @param y
     *            �¼ӵ��y����
     */
    public void updateChart(double x, double y) {
        mSeries.add(x, y);
        mGraphicalView.repaint();//�˴�Ҳ���Ե���invalidate()
    }

    /**
     * ����µ����ݣ����飬�������ߣ�ֻ�����������߳�
     * @param xList
     * @param yList
     */
    public void updateChart(List<Double> xList, List<Double> yList) {
        for (int i = 0; i < xList.size(); i++) {
            mSeries.add(xList.get(i), yList.get(i));
        }
        mGraphicalView.repaint();//�˴�Ҳ���Ե���invalidate()
    }
}