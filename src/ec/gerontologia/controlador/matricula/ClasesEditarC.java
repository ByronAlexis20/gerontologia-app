package ec.gerontologia.controlador.matricula;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Clase;
import ec.gerontologia.modelo.ClasesDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.util.Context;

@SuppressWarnings("serial")
public class ClasesEditarC extends SelectorComposer<Component>{
	@Wire private Window winClaseEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtDescripcion;
	@Wire private Textbox txtObservacion;
	@Wire private Datebox dtpFecha;

	private ClasesListaC claseListaC;
	private ClasesDAO clasesDAO = new ClasesDAO();
	private Clase clase;


	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		claseListaC = (ClasesListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		clase = null; 
		if (Executions.getCurrent().getArg().get("Clase") != null) {
			clase = (Clase) Executions.getCurrent().getArg().get("Clase");
		}else{
			clase = new Clase(); 
		}
	}


	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(txtDescripcion.getText() == null) {
				Clients.showNotification("Por favor ingrese descripción","info",txtDescripcion,"end_center",2000);
				txtDescripcion.focus();
				return resultado;
			}			
			if(txtObservacion.getText() == null) {
				Clients.showNotification("Por favor ingrese observación","info",txtObservacion,"end_center",2000);
				txtObservacion.focus();
				return resultado;
			}
			if(dtpFecha.getValue() == null) {
				Clients.showNotification("Por favor registre la fecha","info",dtpFecha,"end_center",2000);
				return resultado;
			}
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();

	@Listen("onClick=#btnGrabar")
	public void grabar(){
		if (isValidarDatos() == true) {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(ClickEvent event) throws Exception {
					if(Messagebox.Button.YES.equals(event.getButton())) {
						if(isValidarDatos() == true) {//graba o modifica
							Integer idPeriodo = 0;
							List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
							if(encargado.size() > 0) {
								List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
								if(periodos.size() > 0) {
									idPeriodo = periodos.get(0).getIdPeriodo();
								}
							}
							// Inicia la transaccion
							clasesDAO.getEntityManager().getTransaction().begin();
							clase.setEstado("A");
							clase.setIdUsuario(Context.getInstance().getUsuarioLogeado().getIdUsuario());
							clase.setIdPeriodo(idPeriodo);
							if (clase.getIdClase() == null) {
								clasesDAO.getEntityManager().persist(clase);
							}else{
								clase = (Clase) clasesDAO.getEntityManager().merge(clase);
							}
							// Cierra la transaccion.
							clasesDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							claseListaC.buscar();			
							salir(); 			
						}else{
							Clients.showNotification("Verifique que los campos esten llenos.");
						}
					}
				}
			};
			Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
					Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winClaseEditar.detach();
	}


	public Clase getClase() {
		return clase;
	}


	public void setClase(Clase clase) {
		this.clase = clase;
	}


	@SuppressWarnings("unused")
	private void showNotify(String msg, Component ref) {
		Clients.showNotification(msg, "info", ref, "end_center", 2000);
	}
}
