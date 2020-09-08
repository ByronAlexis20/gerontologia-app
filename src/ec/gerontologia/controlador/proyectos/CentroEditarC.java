package ec.gerontologia.controlador.proyectos;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.CentroDAO;
import ec.gerontologia.modelo.Empresa;
import ec.gerontologia.modelo.EmpresaDAO;

@SuppressWarnings("serial")
public class CentroEditarC extends SelectorComposer<Component>{
	@Wire private Window winCentroEditar;
	@Wire private Textbox txtEmpresa;
	@Wire private Textbox txtCodigo;
	@Wire private Textbox txtNombre;
	@Wire private Textbox txtDireccion;
	
	private CentrosListaC centroListaC;
	private CentroDAO centroDAO = new CentroDAO();
	private Centro centro;
	EmpresaDAO empresaDAO = new EmpresaDAO();
	
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		List<Empresa> lista = empresaDAO.getEmpresaActivo();
		if(lista.size() > 0)
			txtEmpresa.setText(lista.get(0).getRazonSocial());

		centroListaC = (CentrosListaC)Executions.getCurrent().getArg().get("VentanaPadre");
		centro = null; 
		if (Executions.getCurrent().getArg().get("Centro") != null) {
			centro = (Centro) Executions.getCurrent().getArg().get("Centro");
		}else{
			centro = new Centro(); 
		}
	}
	

	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(txtNombre.getText() == null) {
				Clients.showNotification("Por favor ingrese nombre del centro.!!");
				txtNombre.focus();
				return resultado;
			}			
			if(txtDireccion.getText() == null) {
				Clients.showNotification("Por favor ingrese dirección del centro.!!");
				txtDireccion.focus();
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
				centroDAO.getEntityManager().getTransaction().begin();
				centro.setEstado("A");
				if (centro.getIdCentro() == null) {
					List<Empresa> lista = empresaDAO.getEmpresaActivo();
					if(lista.size() > 0)
						centro.setEmpresa(lista.get(0));
					centroDAO.getEntityManager().persist(centro);
				}else{
					centro = (Centro) centroDAO.getEntityManager().merge(centro);
				}
				// Cierra la transaccion.
				centroDAO.getEntityManager().getTransaction().commit();
				Clients.showNotification("Proceso Ejecutado con exito.");
				centroListaC.buscar();			
				salir(); 			
			}else{
				Clients.showNotification("Verifique que los campos esten llenos.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			centroDAO.getEntityManager().getTransaction().rollback();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winCentroEditar.detach();
	}


	public Centro getCentro() {
		return centro;
	}


	public void setCentro(Centro centro) {
		this.centro = centro;
	}

}
