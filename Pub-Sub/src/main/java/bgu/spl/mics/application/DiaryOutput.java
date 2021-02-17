package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;

public class DiaryOutput {
    private List<Report> reports;
    private int total;

    public DiaryOutput(List<Report> reports, int total) {
        this.reports = reports;
        this.total = total;
    }


}
