package com.pfe.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import com.pfe.DAO.CourrierArriveDAO;
import com.pfe.DAO.FichierDAO;
import com.pfe.DAO.HistoriqueDAO;
import com.pfe.DAO.OpenCMIS;
import com.pfe.DAO.RestApi;
import com.pfe.entities.CourrierArrive;
import com.pfe.entities.Employe;
import com.pfe.entities.Etablissement;
import com.pfe.entities.Fichier;
import com.pfe.entities.Historique;
import com.pfe.entities.ServiceExterne;

@ManagedBean( name = "historiqueMB" )
@ViewScoped
public class HistoriqueMB {

    @ManagedProperty( value = "#{connexion}" )
    private Connexion        cnx;
    private Employe               employe               = new Employe();
    private HistoriqueDAO    historiqueDAO = new HistoriqueDAO();
    private String                selectedType          = "Mot cle";
   

    private List<CourrierArrive>  courriersArrives      = new ArrayList<CourrierArrive>();
    private String                priorite;
    private Date                  date1;
    private Date                  date2;
    private int                   selectedEtablissement = 0;
    private int                   selectedService       = 0;
    private List<String>          selectedTags          = new ArrayList<String>();
    private String                espace;
    private String                text   ;
    private List<CourrierArrive>        courriersAdmin        ;
    CourrierArriveDAO             courrierArriveDAO     = new CourrierArriveDAO();
    private CourrierArrive        courrierArrive        = new CourrierArrive();
    private List<ServiceExterne>  servicesExternes      = new ArrayList<ServiceExterne>();
    private List<Etablissement>   etablissements        = new ArrayList<Etablissement>();
    private List<Historique> historiques;
    private List<Historique> courriersTraites;
    
 


	public List<Historique> getCourriersTraites() {
		return courriersTraites;
	}
	public void setCourriersTraites(List<Historique> courriersTraites) {
		this.courriersTraites = courriersTraites;
	}


	private RestApi restApi;
    private OpenCMIS openCMIS;
    FichierDAO fichierDAO = new FichierDAO();
    Fichier fichier;
    
    public FichierDAO getFichierDAO() {
		return fichierDAO;
	}
	public void setFichierDAO(FichierDAO fichierDAO) {
		this.fichierDAO = fichierDAO;
	}
	public Fichier getFichier() {
		return fichier;
	}
	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}
	public void initEtablissements() {
        etablissements = courrierArriveDAO.getEtablissement();
    }
    public void initAlfresco(){
    	 
    		restApi = new RestApi(cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
    		 openCMIS = new OpenCMIS(cnx.getEmploye(),cnx.getAlfrescoServer(),cnx.getAlfrescoPort());
    	}

    
    
    public String getTicket() throws UnsupportedOperationException,
	org.json.JSONException, IOException {


return restApi.getTicket();
}
    // intialiser les services suivant l'etablissement selectionné
    public void initService( AjaxBehaviorEvent e ) {
        servicesExternes = courrierArriveDAO.getServiceExterne( selectedEtablissement );
    }
    public List<String> completeObjet( String query ) {
        
        List<String> texts=null;
        if(selectedType.equals("Reference"))
        {
        	 texts = courrierArriveDAO.getRef(query);
        	
        }
        else{
        	 texts = courrierArriveDAO.getObjet( query );
        }
     
 //      
        return texts;
    }

    public List<String> completeTag( String query ) {
        HashSet<String> tags = courrierArriveDAO.getTags( query );
        List<String> list = new ArrayList<String>( tags );
        return list;
    }
    
    public List<Etablissement> getEtablissements() {
		return etablissements;
	}

	public void setEtablissements(List<Etablissement> etablissements) {
		this.etablissements = etablissements;
	}

	public List<ServiceExterne> getServicesExternes() {
		return servicesExternes;
	}

	public void setServicesExternes(List<ServiceExterne> servicesExternes) {
		this.servicesExternes = servicesExternes;
	}

	public Employe getFirstUser( String ref ) {
        Employe employe = courrierArriveDAO.getFirstUser( ref );
        return employe;
    }
    
    public Date getCreationDate( String ref ) {
        Date date = courrierArriveDAO.getCreationDate( ref );

        return date;
    }
    
	public void initCourriersTraites(){
		courriersTraites=historiqueDAO.getCourriersTraites(cnx.getEmploye(),"traité");
	}

    public void init(){
    	historiques =  courrierArriveDAO.GetHistorique( "2015-05-25-7" ); 
    	
    	
    }
    public List<Historique> getHistoriques() {
        return historiques;
    }

    public void setHistoriques( List<Historique> historiques ) {
        this.historiques = historiques;
    }

    public Connexion getCnx() {
        return cnx;
    }

    public void setCnx( Connexion cnx ) {
        this.cnx = cnx;
    }
    
    public Employe getEmploye() {
		return employe;
	}

	public void setEmploye(Employe employe) {
		this.employe = employe;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public List<CourrierArrive> getCourriersArrives() {
		return courriersArrives;
	}

	public void setCourriersArrives(List<CourrierArrive> courriersArrives) {
		this.courriersArrives = courriersArrives;
	}

	public String getPriorite() {
		return priorite;
	}

	public void setPriorite(String priorite) {
		this.priorite = priorite;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public int getSelectedEtablissement() {
		return selectedEtablissement;
	}

	public void setSelectedEtablissement(int selectedEtablissement) {
		this.selectedEtablissement = selectedEtablissement;
	}

	public int getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(int selectedService) {
		this.selectedService = selectedService;
	}

	public List<String> getSelectedTags() {
		return selectedTags;
	}

	public void setSelectedTags(List<String> selectedTags) {
		this.selectedTags = selectedTags;
	}

	public String getEspace() {
		return espace;
	}

	public void setEspace(String espace) {
		this.espace = espace;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	

	public List<CourrierArrive> getCourriersAdmin() {
		return courriersAdmin;
	}
	public void setCourriersAdmin(List<CourrierArrive> courriersAdmin) {
		this.courriersAdmin = courriersAdmin;
	}
	public CourrierArrive getCourrierArrive() {
		return courrierArrive;
	}

	public void setCourrierArrive(CourrierArrive courrierArrive) {
		this.courrierArrive = courrierArrive;
	}

	public void rechercher() {
      

            if ( selectedType.equals( "Mot cle" ) ) {
                courriersAdmin = courrierArriveDAO.getCourriersAdmin(  date1, date2, selectedEtablissement,
                        selectedService, selectedTags, selectedType, priorite );

            }
            else {
                courriersAdmin = courrierArriveDAO.getCourriersAdmin( date1, date2, selectedEtablissement,
                        selectedService, text, selectedType, priorite );
            }

        }
    
	
	public void getAttribute(ActionEvent event)
			throws UnsupportedOperationException, org.json.JSONException,
			IOException {

		courrierArrive = (CourrierArrive) event.getComponent().getAttributes()
				.get("selectedItem");
		
		fichier = fichierDAO.getFichier(courrierArrive);
	
		

	}


    public void voirHistorique( ActionEvent event ) {
      
      
        historiques = courrierArriveDAO.GetHistorique( courrierArrive.getRef());
        courrierArrive = historiques.get( 0 ).getCourrierArr();
        
       for(Historique h : historiques){
    	   
       }
     
    }
    
}
