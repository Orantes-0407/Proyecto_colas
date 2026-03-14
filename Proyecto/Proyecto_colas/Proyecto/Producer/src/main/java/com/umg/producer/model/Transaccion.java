package com.umg.producer.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaccion {
	private String IdTransaccion;
	private double monto;
	private String moneda;
	private String cuentaOrigen;
	private String bancoDestino;
	private Detalle detalle;
	
	public Transaccion() {
	}
	public String getIdTransaccion() {
		return IdTransaccion;
	}
	public void setIdTransaccion(String idTransaccion) {
		IdTransaccion = idTransaccion;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}	
	public String getCuentaOrigen() {
		return cuentaOrigen;
	}
	public String getBancoDestino() {
		return bancoDestino;
	}	
	public void setBancoDestino(String bancoDestino) {
		this.bancoDestino = bancoDestino;
	}
	public Detalle getDetalle() {
		return detalle;
	}
	public void setDetalle(Detalle detalle) {
		this.detalle = detalle;
	}
	
	

}
