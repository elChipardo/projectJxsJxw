package Files;

public class File {

    String nameFile;
    String idFile;
    String namePlateforme;
    String sharePerson;
    String dateModif;

    public File (String nF, String iF, String nP, String sP, String dM){
        nameFile=nF;
        idFile=iF;
        namePlateforme=nP;
        sharePerson=sP;
        dateModif=dM;

    }

    @Override
    public String toString() {
        return "name : "+nameFile + "id : " + idFile + "namePlateforme : " + namePlateforme + "sharePerson : " + sharePerson + "datemodif : " + dateModif;
    }
}
