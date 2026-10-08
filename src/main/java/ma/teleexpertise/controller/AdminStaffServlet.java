package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.Role;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.service.AdminService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(urlPatterns = {
    "/admin/staff",
    "/admin/staff/creer",
    "/admin/staff/supprimer"
})
public class AdminStaffServlet extends HttpServlet {

    private final AdminService adminService = new AdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(request.getSession()));
        List<User> staff = adminService.getAllStaff();
        request.setAttribute("staffList", staff);
        request.setAttribute("roles", Arrays.asList(Role.values()));
        request.setAttribute("specialites", Arrays.asList(Specialite.values()));

        request.getRequestDispatcher("/WEB-INF/views/admin/staff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String ctx = request.getContextPath();

        try {
            if ("/admin/staff/creer".equals(path)) {
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                Role role = Role.valueOf(request.getParameter("role"));

                Specialite specialite = null;
                Double tarif = null;
                if (role == Role.SPECIALISTE) {
                    String specStr = request.getParameter("specialite");
                    if (specStr != null && !specStr.isEmpty()) {
                        specialite = Specialite.valueOf(specStr);
                    }
                    String tarifStr = request.getParameter("tarif");
                    if (tarifStr != null && !tarifStr.isEmpty()) {
                        tarif = Double.parseDouble(tarifStr.replace(',', '.'));
                    }
                }

                adminService.createStaffMember(username, email, password, nom, prenom, role, specialite, tarif);
                response.sendRedirect(ctx + "/admin/staff?success=1");
                return;
            }

            if ("/admin/staff/supprimer".equals(path)) {
                Long id = Long.parseLong(request.getParameter("id"));
                adminService.deactivateStaff(id);
                response.sendRedirect(ctx + "/admin/staff?deactivated=1");
                return;
            }

        } catch (Exception e) {
            request.setAttribute("error", "Erreur : " + e.getMessage());
            doGet(request, response);
            return;
        }

        response.sendRedirect(ctx + "/admin/staff");
    }
}
