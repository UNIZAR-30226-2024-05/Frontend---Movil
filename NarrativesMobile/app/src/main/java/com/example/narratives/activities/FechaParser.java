package com.example.narratives.activities;

public class FechaParser {
    String fecha;

    int año;
    int mes;
    int dia;
    int hora;
    int minuto;
    int segundo;
    int milisegundo;
    int timezone;

    public FechaParser(String s){
        String[] parseFechaHora = s.split(" ");
        String _fecha = parseFechaHora[0];
        String _hora = parseFechaHora[1];
        String[] parseFecha = _fecha.split("-");
        String[] parseHora = _hora.split(":");
        String[] parseSeg = parseHora[2].split("\\.");
        String[] parseMsTz = parseSeg[1].split("\\+");

        año = Integer.parseInt(parseFecha[0]);
        mes = Integer.parseInt(parseFecha[1]);
        dia = Integer.parseInt(parseFecha[2]);

        hora = Integer.parseInt(parseHora[0]);
        minuto = Integer.parseInt(parseHora[1]);
        segundo = Integer.parseInt(parseSeg[0]);
        milisegundo = Integer.parseInt(parseMsTz[0]);
        milisegundo = Integer.parseInt(parseMsTz[1]);
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSegundo() {
        return segundo;
    }

    public void setSegundo(int segundo) {
        this.segundo = segundo;
    }

    public int getMilisegundo() {
        return milisegundo;
    }

    public void setMilisegundo(int milisegundo) {
        this.milisegundo = milisegundo;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }
}
