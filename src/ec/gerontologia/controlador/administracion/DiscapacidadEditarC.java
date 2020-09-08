package ec.gerontologia.controlador.administracion;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Discapacidad;
import ec.gerontologia.modelo.DiscapacidadDAO;

@SuppressWarnings("serial")
public class DiscapacidadEditarC extends SelectorComposer<Component>{
	@Wire private Window winDiscapacidadEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	
	private DiscapacidadListaC discapacidadListaC;
	private DiscapacidadDAO discapacidadDAO = new DiscapacidadDAO();
	private Discapacidad discapacidad;
	
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		discapacidadListaC = (DiscapacidadListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		discapacidad = null; 
		if (Executions.getCurrent().getArg().get("Discapacidad") != null) {
			discapacidad = (Discapacidad) Executions.getCurrent().getArg().get("Discapacidad");
		}else{
			discapacidad = new Discapacidad(); 
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
				discapacidadDAO.getEntityManager().getTransaction().begin();
				discapacidad.setEstado("A");
				if (discapacidad.getIdDiscapacidad() == null) {
					discapacidadDAO.getEntityManager().persist(discapacidad);
				}else{
					discapacidad = (Discapacidad) discapacidadDAO.getEntityManager().merge(discapacidad);
				}
				// Cierra la transaccion.
				discapacidadDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				discapacidadListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			discapacidadDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winDiscapacidadEditar.detach();
	}


	public Discapacidad getDiscapacidad() {
		return discapacidad;
	}


	public void setDiscapacidad(Discapacidad discapacidad) {
		this.discapacidad = discapacidad;
	}

}
