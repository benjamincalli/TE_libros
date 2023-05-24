package com.emergentes.controlador;

import com.emergentes.modelo.Libro;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String op;
        op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
        ArrayList<Libro> lista = new ArrayList<Libro>();
        ConexionDB canal = new ConexionDB();
        Connection conn = canal.conectar();
        PreparedStatement ps;
        ResultSet rs;

        if (op.equals("list")) {
            try {
                String sql = "select * from libros ";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Libro lib = new Libro();
                    lib.setId(rs.getInt("id"));
                    lib.setIsbn(rs.getString("isbn"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));
                    lista.add(lib);
                }
                request.setAttribute("lista", lista);
                request.getRequestDispatcher("index.jsp").forward(request, response);

            } catch (SQLException e) {
                System.out.println("eroor al contextar" + e.getMessage());
            } finally {
                canal.desconectar();
            }
        }

        if (op.equals("nuevo")) {

            Libro li = new Libro();
            System.out.println(li.toString());
            request.setAttribute("libro", li);

            request.getRequestDispatcher("editar.jsp").forward(request, response);
        }
        
        if (op.equals("editar")) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));

                String sql = "select * from libros where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                Libro li = new Libro();

                while (rs.next()) {
                    li.setId(rs.getInt("id"));
                    li.setIsbn(rs.getString("isbn"));
                    li.setTitulo(rs.getString("titulo"));
                    li.setCategoria(rs.getString("categoria"));
                    // Asignar valores a otros campos si es necesario
                }
                request.setAttribute("libro", li);

                request.getRequestDispatcher("editar.jsp").forward(request, response);
            } catch (SQLException e) {
                System.out.println("eroor al contextar" + e.getMessage());
            } finally {
                canal.desconectar();
            }
        }
        if (op.equals("eliminar")) {
            try {

                int id = Integer.parseInt(request.getParameter("id"));

                String sql = "delete from libros where id = ? ";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("eroor al contextar" + e.getMessage());
            } finally {
                canal.desconectar();
            }
            response.sendRedirect("MainController");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("Valor de ID " + id);
        String isbn = request.getParameter("isbn");
        String titulo = request.getParameter("titulo");
        String categoria = request.getParameter("categoria");

        Libro lib = new Libro();
        lib.setId(id);
        lib.setIsbn(isbn);
        lib.setTitulo(titulo);
        lib.setCategoria(categoria);

        ConexionDB canal = new ConexionDB();
        Connection conn = canal.conectar();
        PreparedStatement ps;
        ResultSet rs;

        if (id == 0) {

            String sql = "insert into libros (isbn,titulo,categoria)values(?,?,?)";
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getIsbn());
                ps.setString(2, lib.getTitulo());
                ps.setString(3, lib.getCategoria());

                ps.executeUpdate();
            } catch (SQLException e) {
                System.out.println("eroor al contextar" + e.getMessage());
            } finally {
                canal.desconectar();
            }
            response.sendRedirect("MainController");
        } 
        else {
            try {
                String sql = "update libros set isbn=?,titulo=?,categoria=? where id=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getIsbn());
                ps.setString(2, lib.getTitulo());
                ps.setString(3, lib.getCategoria());
                ps.setInt(4, lib.getId());
                ps.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Error en SQL" + e.getMessage());
            }
            response.sendRedirect("MainController");
        }
    }
}
