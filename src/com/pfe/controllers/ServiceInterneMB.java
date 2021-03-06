package com.pfe.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import com.pfe.DAO.OpenCMIS;
import com.pfe.DAO.RestApi;
import com.pfe.DAO.ServiceInterneDAO;
import com.pfe.entities.ServiceInterne;

@ManagedBean(name = "serviceInterneMB")
@ViewScoped
public class ServiceInterneMB {
	@ManagedProperty(value = "#{connexion}")
	private Connexion cnx;

	public Connexion getCnx() {
		return cnx;
	}

	public void setCnx(Connexion cnx) {
		this.cnx = cnx;
	}

	private ServiceInterne serviceInterne = new ServiceInterne();

	private List<ServiceInterne> servicesInternes = new ArrayList<ServiceInterne>();
	private List<ServiceInterne> filteredServices = new ArrayList<ServiceInterne>();
	private OpenCMIS openCMIS;
	private ServiceInterneDAO serviceInterneDAO = new ServiceInterneDAO();
private String selectedName =null;
	// Initialiser le "DataTable" avec la liste des services
	public void initDataTable() {
		servicesInternes = serviceInterneDAO.lister();
		
	}

	public void initAlfresco() {

		openCMIS = new OpenCMIS(cnx.getEmploye(), cnx.getAlfrescoServer(),
				cnx.getAlfrescoPort());
	}

	// Récupérer l'id du service à modifier et initialiser le formulaire avec
	// ses informations
	public void initForm() throws IOException {
		
		
		
		int id;
	     try {
				id = Integer.parseInt(FacesContext.getCurrentInstance()
						.getExternalContext().getRequestParameterMap()
						.get("id"));

			} catch (java.lang.NumberFormatException ex) {
				// erreur form_id
				id = 0;
				FacesContext.getCurrentInstance().getExternalContext()
						.dispatch("/faces/presets/failService.xhtml");

			}
	     serviceInterne = serviceInterneDAO.consulter(id);
			if (     serviceInterne == null) {
				

				FacesContext.getCurrentInstance().getExternalContext()
						.dispatch("/faces/presets/failService.xhtml");

			} 
			else{
				selectedName=serviceInterne.getNom();
			}
	}

	// Retourner la liste des services recherchés par l'utilisateur
	public List<ServiceInterne> completeService(String query) {
		List<ServiceInterne> servicesInternes = serviceInterneDAO.lister();
		List<ServiceInterne> filteredServices = new ArrayList<ServiceInterne>();

		for (int i = 0; i < servicesInternes.size(); i++) {
			ServiceInterne serviceInterne = servicesInternes.get(i);
			if (serviceInterne.getNom().toLowerCase().startsWith(query)) {
				filteredServices.add(serviceInterne);
			}
		}
		return filteredServices;
	}

	
	public void validateServiceInterne(FacesContext ctx, UIComponent component,
			Object value) throws ValidatorException {
		
		String name = value.toString();
		boolean result= serviceInterneDAO.findService(name);
		if (result == true&&!name.equals(selectedName)) {
			FacesMessage msg = 
					new FacesMessage("erreur.", 
							"service deja existe");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				
				throw new ValidatorException(msg);

		
		}
		
	}
	// Ajouter un service
	public String add() {
		serviceInterneDAO.ajouter(serviceInterne);

		openCMIS.createServiceFolder(serviceInterne);
		return "listerServices.xhtml?faces-redirect=true";
	}

	// Modifier un service
	public String edit() {
		if(!selectedName.equals(serviceInterne.getNom())){
			openCMIS.changeServiceFolder(selectedName, serviceInterne);
		}
		serviceInterneDAO.modifier(serviceInterne);
		
		return "listerServices.xhtml?faces-redirect=true";
	}

	// Supprimer un service
	public void delete(ActionEvent event) {
		
		serviceInterne = (ServiceInterne) event.getComponent().getAttributes()
				.get("selectedItem");
		openCMIS.removeServiceFolder(serviceInterne);
		
		servicesInternes.remove(serviceInterne);
		
		serviceInterneDAO.supprimer(serviceInterne);
		
	}

	// Rechercher des services
	public void search(ActionEvent actionEvent) {
		if (serviceInterne == null)
			servicesInternes = serviceInterneDAO.lister();
		else
			servicesInternes = serviceInterneDAO
					.select(serviceInterne.getNom());
	}

	public ServiceInterne getServiceInterne() {
		return serviceInterne;
	}

	public void setServiceInterne(ServiceInterne serviceInterne) {
		this.serviceInterne = serviceInterne;
	}

	public List<ServiceInterne> getFilteredServices() {
		return filteredServices;
	}

	public void setFilteredServices(List<ServiceInterne> filteredServices) {
		this.filteredServices = filteredServices;
	}

	public List<ServiceInterne> getServicesInternes() {
		return servicesInternes;
	}

	public void setServicesInternes(List<ServiceInterne> servicesInternes) {
		this.servicesInternes = servicesInternes;
	}
}
