package ec.gerontologia.controlador.administracion;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.UnidadMedica;
import ec.gerontologia.modelo.UnidadMedicaDAO;

@SuppressWarnings("serial")
public class UnidadEditarC extends SelectorComposer<Component>{
	@Wire private Window winUnidadEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtUnidad;
	@Wire private Textbox txtRepresentante;
	
	private UnidadListaC unidadListaC;
	private UnidadMedicaDAO unidadDAO = new UnidadMedicaDAO();
	private UnidadMedica unidad;
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		// Recupera la ventana padre.
		unidadListaC = (UnidadListaC)Executions.getCurrent().getArg().get("VentanaPadre");
		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		unidad = null; 
		if (Executions.getCurrent().getArg().get("Unidad") != null) {
			unidad = (UnidadMedica) Executions.getCurrent().getArg().get("Unidad");
		}else{
			unidad = new UnidadMedica(); 
		}
	}
	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(txtUnidad.getText() == null) {
				Clients.showNotification("Por favor ingrese nombre de la unidad.!!");
				txtUnidad.focus();
				return resultado;
			}
			if(txtRepresentante.getText() == null) {
				Clients.showNotification("Por favor ingrese representante de la unidad.!!");
				txtRepresentante.focus();
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
				unidadDAO.getEntityManager().getTransaction().begin();
				unidad.setEstado("A");
				if (unidad.getIdUnidad() == null) {
					unidadDAO.getEntityManager().persist(unidad);
				}else{
					unidad = (UnidadMedica) unidadDAO.getEntityManager().merge(unidad);
				}
				// Cierra la transaccion.
				unidadDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				unidadListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			unidadDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winUnidadEditar.detach();
	}
	public UnidadMedica getUnidad() {
		return unidad;
	}
	public void setUnidad(UnidadMedica unidad) {
		this.unidad = unidad;
	}
}
