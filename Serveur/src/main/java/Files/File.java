package Files;

public class File {

    String nameFile;
    String type;
    String idFile;
    String namePlateforme;
    String sharePerson;
    String dateModif;

    public File (String nF, String iF, String nP, String sP, String dM, String t){
        nameFile=nF;
        idFile=iF;
        namePlateforme=nP;
        sharePerson=sP;
        dateModif=dM;
        type=t;

    }

    @Override
    public String toString() {
        return "name : "+nameFile + "id : " + idFile + "namePlateforme : " + namePlateforme + "sharePerson : " + sharePerson + "datemodif : " + dateModif + "type : " + type;
    }
}
