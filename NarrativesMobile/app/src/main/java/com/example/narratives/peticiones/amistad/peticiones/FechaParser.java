package com.example.narratives.peticiones.amistad.peticiones;

public class FechaParser {
    String fecha;

    int anyo;
    int mes;
    int dia;
    int hora;
    int minuto;
    int segundo;
    int milisegundo;
    String timezone;

    public FechaParser(String s){
        if(s == null || s == "") {
            this.anyo = 0;
            this.mes = 0;
            this.dia = 0;
            this.hora = 0;
            this.minuto = 0;
            this.segundo = 0;
            this.milisegundo = 0;
            this.timezone = "";
        } else {
            String[] parseFechaHora = s.split("T");
            String _fecha = parseFechaHora[0];
            String _hora = parseFechaHora[1];
            String[] parseFecha = _fecha.split("-");
            String[] parseHora = _hora.split(":");
            String[] parseSeg = parseHora[2].split("\\.");

            anyo = Integer.parseInt(parseFecha[0]);
            mes = Integer.parseInt(parseFecha[1]);
            dia = Integer.parseInt(parseFecha[2]);

            hora = Integer.parseInt(parseHora[0]);
            minuto = Integer.parseInt(parseHora[1]);
            segundo = Integer.parseInt(parseSeg[0]);
            milisegundo = Integer.parseInt(parseSeg[1].substring(0,parseSeg[1].length()-1));
            timezone = "Z";
        }
    }

    public int comparar(FechaParser a2) {
        if (this.getAnyo() > a2.getAnyo()) {
            return -1;
        } else if (this.getAnyo() < a2.getAnyo()) {
            return 1;

        } else if (this.getMes() > a2.getMes()) {
            return -1;
        } else if (this.getMes() < a2.getMes()) {
            return 1;

        } else if (this.getDia() > a2.getDia()) {
            return -1;
        } else if (this.getDia() < a2.getDia()) {
            return 1;

        } else if (this.getHora() > a2.getHora()) {
            return -1;
        } else if (this.getHora() < a2.getHora()) {
            return 1;

        } else if (this.getMinuto() > a2.getMinuto()) {
            return -1;
        } else if (this.getMinuto() < a2.getMinuto()) {
            return 1;

        } else if (this.getSegundo() > a2.getSegundo()) {
            return -1;
        } else if (this.getSegundo() < a2.getSegundo()) {
            return 1;

        } else {
            int mil1, mil2;

            if (this.getMilisegundo() < 10) {
                mil1 = this.getMilisegundo() * 100;
            } else if (this.getMilisegundo() < 100) {
                mil1 = this.getMilisegundo() * 10;
            } else {
                mil1 = this.getMilisegundo();
            }

            if (a2.getMilisegundo() < 10) {
                mil2 = a2.getMilisegundo() * 100;
            } else if (a2.getMilisegundo() < 100) {
                mil2 = a2.getMilisegundo() * 10;
            } else {
                mil2 = a2.getMilisegundo();
            }

            if (mil1 > mil2) {
                return -1;
            } else if (mil1 < mil2) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public String getFechaFormateada() {
        String minStr;
        String segStr;
        String msStr;

        if (this.getMinuto() == 0) {
            minStr = "00";
        } else if (this.getMinuto() < 10) {
            minStr = "0" + String.valueOf(this.getMinuto());
        } else {
            minStr = String.valueOf(this.getMinuto());
        }

        if (this.getSegundo() == 0) {
            segStr = "00";
        } else if (this.getSegundo() < 10) {
            segStr = "0" + String.valueOf(this.getSegundo());
        } else {
            segStr = String.valueOf(this.getSegundo());
        }

        if(this.getMilisegundo() == 0){
            msStr = "000";
        }else if(this.getMilisegundo() < 10){
            msStr = String.valueOf(this.getMilisegundo()) + "00";
        } else if(this.getMilisegundo() < 100){
            msStr = String.valueOf(this.getMilisegundo()) + "0";
        } else {
            msStr = String.valueOf(this.getMilisegundo());
        }


            return String.valueOf(this.getDia()) + "/" + String.valueOf(this.getMes()) + "/" + String.valueOf(this.getAnyo()) +
                "\na las\n" + String.valueOf(this.getHora()+2) + ":" + minStr + ":" + segStr + "." + msStr;

    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getAnyo() {
        return anyo;
    }

    public void setAnyo(int anyo) {
        this.anyo = anyo;
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
