import { Outlet, Link } from "react-router-dom";
import {
  AppBar,
  Toolbar,
  Typography,
  Box,
  Drawer,
  List,
  ListItemButton,
  ListItemText,
} from "@mui/material";

export default function OwnerLayout() {
  return (
    <Box sx={{ display: "flex" }}>
      <AppBar position="fixed">
        <Toolbar>
          <Typography variant="h6">Restaurant Owner</Typography>
        </Toolbar>
      </AppBar>

      <Drawer variant="permanent" sx={{ width: 240 }}>
        <Toolbar />
        <List>
          <ListItemButton component={Link} to="/owner">
            <ListItemText primary="Dashboard" />
          </ListItemButton>
          <ListItemButton component={Link} to="/owner/menu">
            <ListItemText primary="Menu" />
          </ListItemButton>
          <ListItemButton component={Link} to="/owner/orders">
            <ListItemText primary="Orders" />
          </ListItemButton>
        </List>
      </Drawer>

      <Box sx={{ flexGrow: 1, p: 3, ml: 30 }}>
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  );
}
