package ec.gerontologia.controlador.proyectos;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.CentroDAO;

@SuppressWarnings("serial")
public class SeleccionarCentroC extends SelectorComposer<Component>{
	@Wire private Window winSeleccionarCentroLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstCentro;
	
	private List<Centro> centro;
	private Centro centroSeleccionado;
	CentroDAO centroDAO = new CentroDAO();
	ResponsableCentroC responsable;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		buscar();
		responsable = (ResponsableCentroC)Executions.getCurrent().getArg().get("VentanaPadre");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		if (centro != null) {
			centro = null; 
		}
		centro = centroDAO.getCentros(txtBuscar.getValue());
		lstCentro.setModel(new ListModelList(centro));
		centroSeleccionado = null;
	}
	@Listen("onClick=#btnAceptar")
	public void aceptar(){
		if(centroSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		responsable.recuperarCentro(centroSeleccionado);
		salir();
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winSeleccionarCentroLista.detach();
	}
	public List<Centro> getCentro() {
		return centro;
	}
	public void setCentro(List<Centro> centro) {
		this.centro = centro;
	}
	public Centro getCentroSeleccionado() {
		return centroSeleccionado;
	}
	public void setCentroSeleccionado(Centro centroSeleccionado) {
		this.centroSeleccionado = centroSeleccionado;
	}
}
