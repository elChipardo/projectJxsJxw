package Files;

public class File {

    String nameFile;
    String type;
    String idFile;
    String namePlateforme;
    String sharePerson;
    String dateModif;
    long size;

    public File (String nF, String iF, String nP, String sP, String dM, String t, long s){
        nameFile=nF;
        idFile=iF;
        namePlateforme=nP;
        sharePerson=sP;
        dateModif=dM;
        type=t;
        size=s;


    }

    @Override
    public String toString() {
        return "name : "+nameFile + "id : " + idFile + "namePlateforme : " + namePlateforme + "sharePerson : " + sharePerson + "datemodif : " + dateModif + "type : " + type;
    }
}
