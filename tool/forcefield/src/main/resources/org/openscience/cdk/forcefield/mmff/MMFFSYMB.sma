* SMARTS patterns for matching MMFF symbolic atom types. 
*
* The contents of this file was initially derived from the MMFF94 implementation
* in Jmol (http://jmol.sourceforge.net/) licensed as LGPL.
*
* Some atom types not seen in the validation suite have not been assigned patterns.
* Unvalidated types are: NN=C, NPO3, ON=O, NC%C, N5A+, N5OX, O4S, O4P, COON, N5+,
*                        OSO, H3N, CSP2, and ZINC
*
* Aliphatic types that are not assigned patterns in this file are:              
* - CSP2 GENERIC SP2 CARBON
* - ZINC DIPOSITIVE ZINC 
* - NN=C NITROGEN IN N-N=C
[CX4r3] CR3R
[CX4r4] CR4R
[CX4] CR
[CX3r4]=C CE4R
[CX3]=C C=C
[CX3](=[OX1])[OX1] CO2M
[CX3](=[SX1])[SX1] CS2M
[CX3](=[NX3v4]~[!O])([NX3])[NX3] CGD+
[CX3](=[NX3v4]~[!O])[NX3] CNN+
[CX3](=N)([NX3])[NX3] CGD
[CX3](=O)(O)O COOO
[CX3](=O)(O)[NX3H2] COON
[CX3](=O)(N)[NX3] CONN
[CX3](=O)N C=ON
[CX3](=O)S C=OS
[CX3]=S(=O)=O CSO2
[CX3]=S=O CS=O
[CX3](=S)[NX3] C=SN
[CX3](=S)S CSS
[CX3](=O)([#1,C])[#1,C] C=OR
[CX3](=[OX1])(O)[#1,C] COO
[CX3]=O C=O
[CX3]=N C=N
[CX3]=P C=P
[CX3]=S C=S
[CX1]#N C%
[CX2]#* CSP
[CX2](=*)=* =C=
[NX4]~[OX1] N3OX
[NX4] NR+
[NX3v4](~[OX1])(~O)~O NO3
[NX3v4](~[OX1])~[OX1] NO2
[NX3v4](~[OX1]) N2OX
[N+]=C([NX3])[NX3] NGD+
[NX3]C(=[N+])[NX3] NGD+
[NX3v4]=C[NX3v3] NCN+
[NX3v3]C=[NX3v4] NCN+
[NX3v4]=C N+=C
[NX3v4]=N N+=N
[NX3]S(=O)(~[OX1])~O NSO3
[NX3]S(=O)~[OX1] NSO2
[NX3]P(=O)(~[OX1])~O NPO3
[NX3]P(=O)~[OX1] NPO2
[NX3]C#N NC%N
[NX3v3]C=O NC=O
[NX3v3]C=S NC=S
[NX3v3]C=N NC=N
[NX3v3]C=C NC=C
[NX3v3][NX2v3]=[NX2v3] NN=N
[NX3v3]C=P NC=P
[NX3v3]C#[Nv3] NC%C
[NX3] NR
[NX2](=*)=* =N=
[NX2]=C N=C
[NX2]=N N=N
[NX2v3]S(~[OX1])~[OX1] NSO2
[NX2](=O)[C,N] N=O
[NX2]=[SX4](=[OX1])(C)C NSO
[NX2]#[C,N] NR%
[NX2v2] NM
[NX1]#* NSP
[NX1]=[NX2] NAZT
[PX4](~O)(~O)(~O)~O PO4
[PX4](~O)(~O)~O PO3
[PX4](~O)~O PO2 - special case for molecule FAPLUD
[PX4](=O)~S PO2
[PX4]~O PO
[PX4] PTET
[PX3] P
[PX2]=C -P=C
[OX3] O+
[OX2H2] OH2
[OX2]~P(~O)(~O)~O OPO3
[OX2]~P(~O)~O OPO2
[OX2]~P~O OPO
[OX2]C=O OC=O
[OX2]S(~[OX1])(~[OX1])~O OSO3
[OX2]S(~[OX1])~[OX1] OSO2
[OX2]N(~[OX1])~[OX1] ONO2
[OX2]S=O OS=O
[OX2]S -OS
[OX2]P -OP
[OX2]-C=S OC=S
[OX2]-[CX3]=[CX3] OC=C
[OX2]-[CX3]=N OC=N
[OX2]C OR
[OX2](-*)-* -O-
[OX2H]-* -O- '*' doesn't match 'H' in the CDK
[OX2]=* O=+
[OX1]=[SX2]=* O=S=
[OX1]~S(~[OX1])(~[OX1])~[OX1] O4S
[OX1]~S(~[OX1])~[OX1] O3S
[OX1]~S~[OX1] O2S
[OX1]~[P](~[OX1])(~[OX1])~[OX1] O4P
[OX1]~[P](~[OX1])~[OX1] O3P
[OX1]~[P]~[OX1] O2P
[OX1]~[P]~[SX1] O2P - special case for molecule FAPLUD
[OX1]~[P] OP
[OX1]~N(~[OX1])~[OX1] O3N
[OX1]~N(~O)~[OX1] O2NO
[OX1]~N~[OX1] O2N
[OX1]~[Nv4] OXN
[OX1]~[CX3]~[OX1] O2CM
[OX1]=[SX3]=[SX1] OSMS
[OX1]=[SX4] O-S
[OX1][ClX4]([OX1])([OX1])[OX1] O4CL
[OX1]=C-N O=CN
[OX1]=C~O O=CO
[OX1]=C(~[#1,C])~[#1,C] O=CR
[OX1]=C O=C
[OX1]=N O=N
[OX1]=S O=S
[OX1][CX3v4] OM2
[OX1][NX2v3] OM2
[OX1] OM
[SX4](=O)(=O)(O)O SO4
[SX4](=O)(=O)O SO3
[SX4](=O)(=O)N SO2N
[SX4](=O)=O SO2
[SX4](=O)=N SNO
[SX3](=*)(=[OX1])=[OX1] =SO2
[SX3](=[SX1])=[OX1] SSOM
[SX3](=[OX1])=[OX1] SO2M
[SX2](=*)=[OX1] =S=O
[SX3](=[NX2]) >S=N
[SX3](=[OX1]) S=O
[SX2](-*)-* S
[SX2H](-*) S
[SX2H2] S
[SX1]~[CX3]~[SX1] S2CM
[SX1]=[CX3] S=C
[SX1]=S=[OX1] SSMO
[SX1]=P S-P
[SX1]-[P+] S-P
[SX1] SM
[FX1] F
[FX0] F-
[BrX1] BR
[BrX0] BR-
[ClX1] CL
[ClX4]([OX1])([OX1])([OX1])[OX1] CLO4
[ClX0] CL-
[IX1] I
[LiX0] LI+
[NaX0] NA+
[MgX0] MG+2
[SiX4] SI
[KX0] K+
[CaX0] CA+2
[FeX0+2] FE+2
[FeX0+3] FE+3
[CuX0+1] CU+1
[CuX0+2] CU+2
[ZnX0+2] ZN+2