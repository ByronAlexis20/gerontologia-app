package ec.gerontologia.controlador.administracion;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Etnia;
import ec.gerontologia.modelo.EtniaDAO;

@SuppressWarnings("serial")
public class IdentidadEditarC extends SelectorComposer<Component>{
	@Wire private Window winIdentidadEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	
	private IdentidadListaC identidadListaC;
	private EtniaDAO etniaDAO = new EtniaDAO();
	private Etnia etnia;
	
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		identidadListaC = (IdentidadListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		etnia = null; 
		if (Executions.getCurrent().getArg().get("Servicio") != null) {
			etnia = (Etnia) Executions.getCurrent().getArg().get("Etnia");
		}else{
			etnia = new Etnia(); 
		}
	}
	

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(txtDescripcion.getText() == null) {
				Clients.showNotification("Por favor ingrese descripción.!!");
				txtDescripcion.focus();
				return resultado;
			}			
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Listen("onClick=#btnGrabar")
	public void grabar(){
		System.out.println("entra grabando");
		try {
			
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				etniaDAO.getEntityManager().getTransaction().begin();
				etnia.setEstado("A");
				if (etnia.getIdEtnia() == null) {
					etniaDAO.getEntityManager().persist(etnia);
				}else{
					etnia = (Etnia) etniaDAO.getEntityManager().merge(etnia);
				}
				// Cierra la transaccion.
				etniaDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				identidadListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			etniaDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winIdentidadEditar.detach();
	}


	public Etnia getEtnia() {
		return etnia;
	}


	public void setEtnia(Etnia etnia) {
		this.etnia = etnia;
	}

}
