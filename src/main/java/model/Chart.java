package model;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class Chart {

    public Chart() {
    }

    public Chart(List<Integer> data, String name, String methodName) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();


        JFrame f = new JFrame(methodName + " chart for " + name);
        f.pack();

        // Create a simple XY chart
        XYSeries series = new XYSeries("");
        for (int i = 0; i < data.size() / 2; i++) {
            series.add(i, data.get(i));
        }
        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Chart", // Title
                "x-axis", // x-axis Label
                "y-axis", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        chart.setTitle("");

        ChartPanel chartPanel = new ChartPanel(chart, false);

        f.setLocationRelativeTo(null);

        f.add(chartPanel);
        chartPanel.setBounds(-100, 0, 900, 600);
        f.setLocationRelativeTo(null);
        f.setBounds((int) (width / 4), (int) (height / 4), 950, 650);
        f.setSize(950, 650);
        f.setVisible(true);

        try {
            ChartUtilities.saveChartAsJPEG(new File("src/main/resources/results/" + methodName + "_" + name + "_chart.jpg"), chart, 1200, 600);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }

    public Chart(long[] data, String name, String methodName) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();


        JFrame f = new JFrame(methodName + " chart for " + name);
        f.pack();

        // Create a simple XY chart
        XYSeries series = new XYSeries("");
        for (int i = 0; i < data.length / 2; i++) {
            series.add(i, data[i]);
        }
        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Chart", // Title
                "x-axis", // x-axis Label
                "y-axis", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        chart.setTitle("");

        ChartPanel chartPanel = new ChartPanel(chart, false);

        f.setLocationRelativeTo(null);

        f.add(chartPanel);
        chartPanel.setBounds(-100, 0, 900, 600);
        f.setLocationRelativeTo(null);
        f.setBounds((int) (width / 4), (int) (height / 4), 950, 650);
        f.setSize(950, 650);
        f.setVisible(true);

        try {
            ChartUtilities.saveChartAsJPEG(new File("src/main/resources/results/" + methodName + "_" + name + "_chart.jpg"), chart, 1200, 600);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }

    public Chart(double[] data, String name, String methodName) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();


        JFrame f = new JFrame(methodName + " chart for " + name);
        f.pack();

        // Create a simple XY chart
        XYSeries series = new XYSeries("");
        for (int i = 0; i < data.length / 2; i++) {
            series.add(i, data[i]);
        }
        // Add the series to your data set
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        // Generate the graph
        JFreeChart chart = ChartFactory.createXYLineChart(
                "XY Chart", // Title
                "x-axis", // x-axis Label
                "y-axis", // y-axis Label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot Orientation
                true, // Show Legend
                true, // Use tooltips
                false // Configure chart to generate URLs?
        );

        chart.setTitle("");

        ChartPanel chartPanel = new ChartPanel(chart, false);

        f.setLocationRelativeTo(null);

        f.add(chartPanel);
        chartPanel.setBounds(-100, 0, 900, 600);
        f.setLocationRelativeTo(null);
        f.setBounds((int) (width / 4), (int) (height / 4), 950, 650);
        f.setSize(950, 650);
        f.setVisible(true);

        try {
            ChartUtilities.saveChartAsJPEG(new File("src/main/resources/results/" + methodName + "_" + name + "_chart.jpg"), chart, 1200, 600);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }

}
















