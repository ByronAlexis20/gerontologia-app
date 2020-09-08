package ec.gerontologia.controlador.administracion;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Servicio;
import ec.gerontologia.modelo.ServicioDAO;

@SuppressWarnings("serial")
public class ServicioEditarC extends SelectorComposer<Component>{
	@Wire private Window winServicioEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	
	private ServicioListaC servicioListaC;
	private ServicioDAO servicioDAO = new ServicioDAO();
	private Servicio servicio;
	
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		servicioListaC = (ServicioListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		servicio = null; 
		if (Executions.getCurrent().getArg().get("Servicio") != null) {
			servicio = (Servicio) Executions.getCurrent().getArg().get("Servicio");
		}else{
			servicio = new Servicio(); 
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
				servicioDAO.getEntityManager().getTransaction().begin();
				servicio.setEstado("A");
				if (servicio.getIdServicio() == null) {
					servicioDAO.getEntityManager().persist(servicio);
				}else{
					servicio = (Servicio) servicioDAO.getEntityManager().merge(servicio);
				}
				// Cierra la transaccion.
				servicioDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				servicioListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			servicioDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winServicioEditar.detach();
	}


	public Servicio getServicio() {
		return servicio;
	}


	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}
	

}
