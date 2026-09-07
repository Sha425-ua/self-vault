{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    openjdk21
    libGL
    fontconfig
    xorg.libX11
    xorg.libXcursor
    xorg.libXrandr
    xorg.libXi
    xorg.libXtst
    xorg.libXxf86vm
  ];

  shellHook = ''
    export JAVA_HOME=${pkgs.openjdk21}/lib/openjdk
    export _JAVA_AWT_WM_NONREPARENTING=1
    export LD_LIBRARY_PATH=/run/opengl-driver/lib:/run/current-system/sw/share/nix-ld/lib:${pkgs.libGL}/lib:${pkgs.fontconfig.lib}/lib:${pkgs.xorg.libX11}/lib:${pkgs.xorg.libXcursor}/lib:${pkgs.xorg.libXrandr}/lib:${pkgs.xorg.libXi}/lib:${pkgs.xorg.libXtst}/lib:${pkgs.xorg.libXxf86vm}/lib:$LD_LIBRARY_PATH
  '';
}
