package ec.gerontologia.modelo;

import java.util.List;

import javax.persistence.Query;

public class EstadoViviendaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<EstadoVivienda> getEstadosVivienda() {
		List<EstadoVivienda> resultado; 
		Query query = getEntityManager().createNamedQuery("EstadoVivienda.buscarEstadosVivienda");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<EstadoVivienda>) query.getResultList();
		return resultado;
	}
}
