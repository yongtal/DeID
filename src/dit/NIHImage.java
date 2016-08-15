package dit;

import com.sun.org.apache.bcel.internal.generic.ISHL;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import niftijlib.Nifti1Dataset;
import niftijlib.Nifti1Dataset_AnalyzeFormat;

/**
 *
 * @author QM
 * brain This class hold a image object, which contains all the
 * attribute of an image.
 * Level:Top
 */
public class NIHImage {

    private File _storedPotistion; //where images are stored
    private File _tempPotision;   //where images are put during the program
    private File _montageFile;    //Not in use
    private String _imageName;      //images' name without extensiont
    private String _imageNewName;   //new id generated for images
    private String _imageDisplayName;  //name shown in the Matching GUI
    private String _imageFormalName;   //name that replace '/' with '_', this make sure in temp dir no filename collision.
    private String _imageFormat;  //images' extension
    private String _idInDataFile;  // the id in data file when matching 
    private String _parentPath;  // if images is chosen from multiple directies, this indicate it's parent file path
    
    //////////////////////////////////////////////////
    ////////////2.13/////////////////////////////////
    //for montage file and the info shows on it.
    private Image _idMontage;
    private Image _deidMontage;
    private Vector<String> _2ndcol;  //save the Second column info no matter what it is showed in the loadDemoPanel,
                                     //[0] is field name (SEX,AGE,IQ...), [1] is value  
    private Vector<String> _3rdcol; 
    private Vector<String> _4thcol;
    
    private boolean _needDefaced;
    private boolean _isDefaced;
    private boolean _isLongitudinal;
    private boolean _seletedInJarFile;
    private boolean _needRedefaced;
    private boolean _isCorrectedOrientation;
    private String _longitudinalSuject = "";
    private String _longitudinalNumber = "";
    private OrientationState orientationState;
    private Nifti1Dataset _set;
    public Nifti1Dataset setAPI;
    private Nifti1Dataset setAnalyze;
    double[][][] _data; // where pixels are stored
    private NiftiPara _para;
    private boolean _originallyGz; //added flag to show if image was originally
        //a compressed file or not
    private boolean _needUpdated;
    private LinkedHashMap _headerMap;
    private boolean _isNiiHdr ;
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////2.13///////////////////////////////////////////
    // set/get identified/Deidentified Image set/get the inforamtion show alongside with montage (the 2nd-4th col in datafile)
    //used in QCpanel, De
    /**
     * @author Xuebo 
     * empty data only
     * 
     */
    public void emptyData() {
        _data = null;
    } 
    /**
     * @author Xuebo
     * empty header
     *     * 
     */
    public void emptyHeader() {
        _set = null;
    }
    
    
    /**
     * @author Xuebo
     * empty header and data
     *
     */
    public void emptySet() {
        _data = null; 
        _set = null;
    }
    
    public boolean isEmpty() {
        return _set == null;       
    }
    
    
    public boolean needUpdated() {
        return _needUpdated;       
    }
    public void setNeedUpdated(boolean updated) {
        this._needUpdated = updated;
    }
    
    public void setIdMontage(Image im){
      _idMontage = im;
    }
    public Image getIdMontage(){
      return _idMontage;
    }
    public void setDeidMontage(Image im){
      _deidMontage = im;
    }
    public Image getDeidMontage(){
      return _deidMontage;
    } 
    public void set2ndCol(String name, String val){
        _2ndcol.add(0,name);
        _2ndcol.add(1,val);
    }
    public void set2ndCol(int index, String val){
        _2ndcol.add(index,val);
    }
    public String get2ndCol(int index){
             return _2ndcol.get(index);
    }
    public void set3rdCol(String name, String val){
        _3rdcol.add(0,name);
        _3rdcol.add(1,val);
    }
    public void set3rdCol(int index, String val){
        _3rdcol.add(index,val);
    }
    public String get3rdCol(int index){
      return _3rdcol.get(index);
    }    
    public void set4thCol(String name, String val){
        _4thcol.add(0,name);
        _4thcol.add(1,val);
    }
    public void set4thCol(int index, String val){
        _4thcol.add(index,val);
    }
    public String get4thCol(int index){
      return _4thcol.get(index);
    }
    public void initMontageInfo(){
       for (int i = 0; i < 3; i++) {
            _2ndcol.add("misV");
            _3rdcol.add("misV");
            _4thcol.add("misV");
       }
    }

    
    public void clearData() {
        _data = new double[0][0][0]; 
    }

    public NIHImage(NIHImage ni){
        this._storedPotistion = ni._storedPotistion;
        this._tempPotision = ni._tempPotision;   //where images are put during the program
        this._montageFile = ni._montageFile;    //Not in use
        this._imageName = ni._imageName;      //images' name without extensiont
        this._imageNewName = ni._imageNewName;   //new id generated for images
        this._imageDisplayName = ni._imageDisplayName;  //name shown in the Matching GUI
        this._imageFormalName = ni._imageFormalName;   //name that replace '/' with '_', this make sure in temp dir no filename collision.
        this._imageFormat = ni._imageFormat;  //images' extension
        this._idInDataFile = ni._idInDataFile;  // the id in data file when matching 
        this._parentPath = ni._parentPath;  // if images is chosen from multiple directies, this indicate it's parent file path
        this._needDefaced = ni._needDefaced;
        this._isDefaced = ni._isDefaced;
        this._needUpdated = ni._needUpdated;
        this._isLongitudinal = ni._isLongitudinal;
        this._seletedInJarFile = ni._seletedInJarFile;
        this._needRedefaced = ni._needRedefaced;
        this._isCorrectedOrientation = ni._isCorrectedOrientation;
        this._longitudinalSuject = ni._longitudinalSuject;
        this._longitudinalNumber = ni._longitudinalNumber;
        this.orientationState = ni.orientationState;
        this._set = ni._set;
        this._data = ni._data; // where pixels are stored
        this._para = ni._para;
        this.setAPI = ni.setAPI;
        this.setAnalyze = ni.setAnalyze;
        this._headerMap = ni._headerMap;
        this._isNiiHdr = ni._isNiiHdr;
        this._2ndcol = new Vector<String>();
        this._3rdcol = new Vector<String>();
        this._4thcol = new Vector<String>();
        initMontageInfo();
        //System.out.println("asdfadfsadffasdfsa");
    
    }
    
    public NIHImage(File file) {
        _storedPotistion = file;
        _tempPotision = null;

        String fileName = file.getAbsolutePath();
        if (fileName.endsWith("nii.gz")) {
            this._originallyGz = true;
            fileName = fileName.replace(".nii.gz", "");
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            this._originallyGz = false;
        }
        _imageName = fileName;
        _imageNewName = "";
        _imageFormalName = FileUtils.getName(file);
        _imageFormat = FileUtils.getExtension(file);
        _parentPath = "";
        _idInDataFile = "";

        _imageDisplayName = _storedPotistion.getAbsolutePath();       

        if (_imageName.matches("^.*#\\d+$")) {
            _isLongitudinal = true;
            _longitudinalSuject = _imageName.substring(0, _imageName.lastIndexOf('#'));
            _longitudinalNumber = _imageName.substring(_imageName.lastIndexOf('#'));
        } else {
            _isLongitudinal = false;
        }
        _needDefaced = true;
        _needRedefaced = false;
        _isDefaced = false;
        _seletedInJarFile = true;
        _isCorrectedOrientation=false;
        _needUpdated = false;
        orientationState = new OrientationState(0, true, 0, 5, 1, 2, 3, 4);
        _headerMap = new LinkedHashMap();
        _2ndcol = new Vector<String>();
        _3rdcol = new Vector<String>();
        _4thcol = new Vector<String>();
        initMontageInfo();
        //System.out.println("asdfadfsadffasdfsa");
    }

    public boolean get_originallyGz(){
        return this._originallyGz;
    }
    
    public void set_originallyGz(boolean _originallyGz){
        this._originallyGz = _originallyGz;
    }

    /**
     * @return the _storedPotistion
     */
    public File getStoredPotistion() {
        return _storedPotistion;
    }
    
    public void initNifti(){
        _set = new Nifti1Dataset(_tempPotision.getAbsolutePath());
        short ttt = 0;
        try {
            _set.readHeader();
            _data = _set.readDoubleVol(ttt);
            _para=new NiftiPara(_set);
            _isNiiHdr = _set.magic.toString().equals("n+1\0") || _set.magic.toString().equals("ni1\0");
        } catch (IOException ex) {
            Logger.getLogger(NIHImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean isNiiHdrFormat () { //must be used after initNifti
        
        return _isNiiHdr;
       
    }
    /**
     * 
     * 
     * 
     */
    private void clearHeaderMap() {
        _headerMap = null;
    }

    /**
     * 
     * @author Xuebo 
     * Convert header content to Linkedhashmap
     * 
     */
    public void createHeader() {
                initNifti();
               
		int i;
		int extlist[][], n;
	
		//System.out.println("\n");
		//System.out.println("Dataset header file:\t\t\t\t"+ds_hdrname);
                _headerMap.put("Dataset header file", _set.ds_hdrname);
		//System.out.println("Size of header:\t\t\t\t\t"+sizeof_hdr);
                _headerMap.put("Size of header", _set.sizeof_hdr);
		//System.out.println("File offset to data blob:\t\t\t"+vox_offset);
                _headerMap.put("File offset to data blob", _set.vox_offset);
		//System.out.print("Endianness:\t\t\t\t\t");
		//if (big_endian)
		//	System.out.println("big");
		//else
		//	System.out.println("little");

		//System.out.println("Magic filetype string:\t\t\t\t"+magic);
                _headerMap.put("Magic filetype string", _set.magic);


		///// Dataset datatype, size, units
		//System.out.println("Datatype:\t\t\t\t\t"+datatype+" ("+decodeDatatype(datatype)+")");
		_headerMap.put("Datatype", _set.datatype);
               //System.out.println("Bits per voxel:\t\t\t\t\t"+bitpix);
		 _headerMap.put("Bits per vokel", _set.bitpix);
                //System.out.println("Scaling slope and intercept:\t\t\t"+scl_slope+" "+scl_inter);
                _headerMap.put("Scaling slope and intercept:", String.valueOf(_set.scl_slope) + " " + String.valueOf(_set.scl_inter));
		//System.out.print("Dataset dimensions (Count, X,Y,Z,T...):\t\t");
                String str = "";
		for (i=0; i<=_set.dim[0]; i++)
                    str += _set.dim[i] + " ";
//	System.out.print(dim[i]+" ");
		//System.out.println("");
                 _headerMap.put("Dataset dimension: (Count, X,Y,Z,T...)", str);
                 
		System.out.print("Grid spacings (X,Y,Z,T,...):\t\t\t");
                str = "";
		for (i=1; i<=_set.dim[0]; i++)
                    str += _set.pixdim[i] + " ";
		//	System.out.print(pixdim[i]+" ");
		//System.out.println("");
                 _headerMap.put("Grid spacings (X,Y,Z,T,...)", str);

		//System.out.println("XYZ  units:\t\t\t\t\t"+xyz_unit_code+" ("+decodeUnits(xyz_unit_code)+")");
                _headerMap.put("XYZ units", _set.xyz_unit_code);
                
		//System.out.println("T units:\t\t\t\t\t"+t_unit_code+" ("+decodeUnits(t_unit_code)+")");
		_headerMap.put("T units", _set.t_unit_code);
                
               // System.out.println("T offset:\t\t\t\t\t"+toffset);
                _headerMap.put("T offset", _set.toffset);

                str = "";
		//System.out.print("Intent parameters:\t\t\t\t");
		for (i=0; i<3; i++)
                        str += _set.intent[i] + " ";
			//System.out.print(intent[i]+" ");
		//System.out.println("");
                _headerMap.put("Intent Parameters", str);
		//System.out.println("Intent code:\t\t\t\t\t"+intent_code+" ("+decodeIntent(intent_code)+")");
                _headerMap.put("Intent code", _set.intent_code);
		
               // System.out.println("Cal. (display) max/min:\t\t\t\t"+cal_max+" "+cal_min);
                _headerMap.put("Cal. (display) max/min", String.valueOf(_set.cal_max) + " " + String.valueOf(_set.cal_min));

		///// Slice order/timing stuff
		//System.out.println("Slice timing code:\t\t\t\t"+slice_code+" ("+decodeSliceOrder((short)slice_code)+")");
                _headerMap.put("Slice timing code", _set.slice_code);
                
		//System.out.println("MRI slice ordering (freq, phase, slice index):\t"+freq_dim+" "+phase_dim+" "+slice_dim);
                _headerMap.put("MRI slice ordering (freq, phase, slice index)", String.valueOf(_set.freq_dim) + " " + String.valueOf(_set.phase_dim) + " " + String.valueOf(_set.slice_dim) );
		//System.out.println("Start/end slice:\t\t\t\t"+slice_start+" "+slice_end);
                _headerMap.put("Strat/end slice", String.valueOf(_set.slice_start) + " " + String.valueOf(_set.slice_end));
                
		//System.out.println("Slice duration:\t\t\t\t\t"+slice_duration);
                _headerMap.put("Slice duration", _set.slice_duration);


		///// Orientation stuff
		//System.out.println("Q factor:\t\t\t\t\t"+qfac);
                _headerMap.put("Q factor", _set.qfac);
                
		//System.out.println("Qform transform code:\t\t\t\t"+qform_code+" ("+decodeXform(qform_code)+")");
                _headerMap.put("Qform transfrom code", _set.qform_code);
                
		//System.out.println("Quaternion b,c,d params:\t\t\t"+quatern[0]+" "+quatern[1]+" "+quatern[2]);
                _headerMap.put("Quaternion b,c,d params", String.valueOf(_set.quatern[0]) + " " + String.valueOf(_set.quatern[1]) + " " + String.valueOf(_set.quatern[2]));
                
		//System.out.println("Quaternion x,y,z shifts:\t\t\t"+qoffset[0]+" "+qoffset[1]+" "+qoffset[2]);
                _headerMap.put("Quaternion x,y,z shifts", String.valueOf(_set.qoffset[0]) + " " + String.valueOf(_set.qoffset[1]) + " " + String.valueOf(_set.qoffset[2]));
                
                //System.out.println("Sform transform code:\t\t\t\t"+sform_code+" ("+decodeXform(sform_code)+")");
                _headerMap.put("Sform/Affine transfrom code", _set.sform_code);
                          
		//System.out.print("1st row affine transform:\t\t\t");
               
                str = "";
		for (i=0; i<4; i++)
                    str += _set.srow_x[i] + " ";
			//System.out.print(srow_x[i]+" ");
                
                 _headerMap.put("1st row affine transfrom", str);
                
                
		//System.out.println("");
		//System.out.print("2nd row affine transform:\t\t\t");
                str = "";
		for (i=0; i<4; i++)
                    str += _set.srow_y[i] + " ";
			//System.out.print(srow_y[i]+" ");
		//System.out.println("");
                 _headerMap.put("2nd row affine transfrom", str);
                 
		//System.out.print("3rd row affine transform:\t\t\t");
                str = "";
		for (i=0; i<4; i++)
                    str += _set.srow_z[i] + " ";
		//	System.out.print(srow_z[i]+" ");
		//System.out.println("");
                 _headerMap.put("3rd row affine transfrom", str);


		///// comment stuff
		//System.out.println("Description:\t\t\t\t\t"+descrip);
                 _headerMap.put("Description", _set.descrip);
                     
		//System.out.println("Intent name:\t\t\t\t\t"+intent_name);
                 _headerMap.put("Intent name", _set.intent_name);
                 
		//System.out.println("Auxiliary file:\t\t\t\t\t"+aux_file);
                 _headerMap.put("Auxiliary file", _set.aux_file);
                
		//System.out.println("Extension byte 1:\t\t\t\t\t"+(int)extension[0]);
                 _headerMap.put("Extension byte 1", (int)_set.extension[0]);


		///// unused stuff
		//System.out.println("\n\nUnused Fields");
		//System.out.println("----------------------------------------------------------------------");
		//System.out.println("Data type string:\t\t\t"+data_type_string);
                _headerMap.put("Data type string", _set.data_type_string);
                               
		//System.out.println("db_name:\t\t\t\t\t"+db_name);
                _headerMap.put("db_name", _set.db_name);
                
		//System.out.println("extents:\t\t\t\t\t"+extents);
                _headerMap.put("extents", _set.extents);
                
		//System.out.println("session_error:\t\t\t\t\t"+session_error);
                _headerMap.put("session_error", _set.session_error);
                
		//System.out.println("regular:\t\t\t\t\t"+regular);
                _headerMap.put("regular", _set.regular);
                
		//System.out.println("glmax/glmin:\t\t\t\t\t"+glmax+" "+glmin);
                _headerMap.put("glmax/glmin", String.valueOf(_set.glmax) + " " + String.valueOf(_set.glmin));
                
		//System.out.println("Extension bytes 2-4:\t\t\t\t"+(int)extension[1]+" "+(int)extension[2]+" "+(int)extension[3]);
                _headerMap.put("Extension bytes 2-4", String.valueOf((int)_set.extension[1]) + " " + String.valueOf((int)_set.extension[2]) + " " + String.valueOf((int)_set.extension[3]));

		
		
		////// extensions
		if (_set.extension[0] != 0) {
			extlist = _set.getExtensionsList();
			n = extlist.length;
			//System.out.println("\n\nExtensions");
			//System.out.println("----------------------------------------------------------------------");
			//System.out.println("#\tCode\tSize");
                        str = "";
			for(i=0; i<n; i++)
                            str += (i+1)+"\t"+extlist[i][1]+"\t"+extlist[i][0] + "\t";
				//System.out.println((i+1)+"\t"+extlist[i][1]+"\t"+extlist[i][0]);
			//System.out.println("\n");
                        _headerMap.put("Extensions\tCode\tSize", str);
                        
		}

        System.out.println("Create Header FIle Successful!!!");
	return;
	}
    public void create_Analyze_Header() {
                initAnalyze();
             
		int i;
		int extlist[][], n;
	
		//System.out.println("\n");
		//System.out.println("Dataset header file:\t\t\t\t"+ds_hdrname);
                _headerMap.put("Dataset header file", setAnalyze.ds_hdrname);
		//System.out.println("Size of header:\t\t\t\t\t"+sizeof_hdr);
                _headerMap.put("Size of header", setAnalyze.sizeof_hdr);
		//System.out.println("File offset to data blob:\t\t\t"+vox_offset);
                _headerMap.put("File offset to data blob", setAnalyze.vox_offset);
		//System.out.print("Endianness:\t\t\t\t\t");
		//if (big_endian)
		//	System.out.println("big");
		//else
		//	System.out.println("little");

		//System.out.println("Magic filetype string:\t\t\t\t"+magic);
                //_headerMap.put("Magic filetype string", setAnalyze.magic);


		///// Dataset datatype, size, units
		//System.out.println("Datatype:\t\t\t\t\t"+datatype+" ("+decodeDatatype(datatype)+")");
		_headerMap.put("Datatype", setAnalyze.datatype);
               //System.out.println("Bits per voxel:\t\t\t\t\t"+bitpix);
		_headerMap.put("Bits per vokel", setAnalyze.bitpix);
                //System.out.println("Scaling slope and intercept:\t\t\t"+scl_slope+" "+scl_inter);
                _headerMap.put("Scaling slope and intercept:", String.valueOf(setAnalyze.scl_slope) + " " + String.valueOf(setAnalyze.scl_inter));
		//System.out.print("Dataset dimensions (Count, X,Y,Z,T...):\t\t");
                String str = "";
		for (i=0; i<=setAnalyze.dim[0]; i++)
                    str += setAnalyze.dim[i] + " ";
//	System.out.print(dim[i]+" ");
		//System.out.println("");
                 _headerMap.put("Dataset dimension: (Count, X,Y,Z,T...)", str);
                 
		//System.out.print("Grid spacings (X,Y,Z,T,...):\t\t\t");
                str = "";
		for (i=1; i<=setAnalyze.dim[0]; i++)
                    str += setAnalyze.pixdim[i] + " ";
		//	System.out.print(pixdim[i]+" ");
		//System.out.println("");
                 _headerMap.put("Grid spacings (X,Y,Z,T,...)", str);

		//System.out.println("XYZ  units:\t\t\t\t\t"+xyz_unit_code+" ("+decodeUnits(xyz_unit_code)+")");
                //_headerMap.put("XYZ units", setAnalyze.xyz_unit_code);
                
		//System.out.println("T units:\t\t\t\t\t"+t_unit_code+" ("+decodeUnits(t_unit_code)+")");
		//_headerMap.put("T units", setAnalyze.t_unit_code);
                
                //System.out.println("Verified:\t\t\t\t\t"+setAnalyze.verified);
                _headerMap.put("Verified", setAnalyze.verified);

               
                _headerMap.put("Intent code", setAnalyze.intent_code);
		
               // System.out.println("Cal. (display) max/min:\t\t\t\t"+cal_max+" "+cal_min);
                _headerMap.put("Cal. (display) max/min", String.valueOf(setAnalyze.cal_max) + " " + String.valueOf(setAnalyze.cal_min));

                ///???
                _headerMap.put("MRI slice ordering (freq, phase, slice index)", String.valueOf(setAnalyze.freq_dim) + " " + String.valueOf(setAnalyze.phase_dim) + " " + String.valueOf(setAnalyze.slice_dim) );
		//System.out.println("Start/end slice:\t\t\t\t"+slice_start+" "+slice_end);
                _headerMap.put("dim un0", String.valueOf(setAnalyze.dim_un0));
                
		//System.out.println("Slice duration:\t\t\t\t\t"+slice_duration);
                _headerMap.put("Compressed", setAnalyze.compressed);

		///// comment stuff
		//System.out.println("Description:\t\t\t\t\t"+descrip);
                 _headerMap.put("Description", setAnalyze.descrip);
                     
		//System.out.println("Intent name:\t\t\t\t\t"+intent_name);
                 //_headerMap.put("Intent name", setAnalyze.intent_name);
                 
		//System.out.println("Auxiliary file:\t\t\t\t\t"+aux_file);
                 _headerMap.put("Auxiliary file", setAnalyze.aux_file);
                 
                 _headerMap.put("Orient",setAnalyze.orient);
                 //_headerMap.put("Originator",setAnalyze.originator);
                 _headerMap.put("Origin 0 1 2",setAnalyze.origin[0]+
                                " "+setAnalyze.origin[1]+" "+setAnalyze.origin[2] );         
                 
                 _headerMap.put("Generated",setAnalyze.generated);
                 _headerMap.put("Scannum",setAnalyze.scannum);
                 _headerMap.put("Patient ID",setAnalyze.patient_id);
                 _headerMap.put("Exp Date",setAnalyze.exp_date);
                 _headerMap.put("Exp Time",setAnalyze.exp_time);
                 _headerMap.put("History",setAnalyze.hist_un0);
                 _headerMap.put("Views",setAnalyze.views);
                 _headerMap.put("Vols added",setAnalyze.vols_added);
                 _headerMap.put("Start field",setAnalyze.start_field);
                 _headerMap.put("Field skip",setAnalyze.field_skip);
                 _headerMap.put("Omax/omin",setAnalyze.omax+" "+setAnalyze.omin);
                 _headerMap.put("Smax/smin",setAnalyze.smax+" "+setAnalyze.smin);
		//System.out.println("Extension byte 1:\t\t\t\t\t"+(int)extension[0]);
                 _headerMap.put("Extension byte 1", (int)setAnalyze.extension[0]);


		///// unused stuff
		//System.out.println("\n\nUnused Fields");
		//System.out.println("----------------------------------------------------------------------");
		//System.out.println("Data type string:\t\t\t"+data_type_string);
                _headerMap.put("Data type string", setAnalyze.data_type_string);
                               
		//System.out.println("db_name:\t\t\t\t\t"+db_name);
                _headerMap.put("db_name", setAnalyze.db_name);
                
		//System.out.println("extents:\t\t\t\t\t"+extents);
                _headerMap.put("extents", setAnalyze.extents);
                
		//System.out.println("session_error:\t\t\t\t\t"+session_error);
                _headerMap.put("session_error", setAnalyze.session_error);
                
		//System.out.println("regular:\t\t\t\t\t"+regular);
                _headerMap.put("regular", setAnalyze.regular);
                
		//System.out.println("glmax/glmin:\t\t\t\t\t"+glmax+" "+glmin);
                _headerMap.put("glmax/glmin", String.valueOf(setAnalyze.glmax) + " " + String.valueOf(setAnalyze.glmin));
                
		//System.out.println("Extension bytes 2-4:\t\t\t\t"+(int)extension[1]+" "+(int)extension[2]+" "+(int)extension[3]);
                _headerMap.put("Extension bytes 2-4", String.valueOf((int)setAnalyze.extension[1]) + " " + String.valueOf((int)setAnalyze.extension[2]) + " " + String.valueOf((int)setAnalyze.extension[3]));

		
		
		////// extensions
		if (setAnalyze.extension[0] != 0) {
			extlist = setAnalyze.getExtensionsList();
			n = extlist.length;
			//System.out.println("\n\nExtensions");
			//System.out.println("----------------------------------------------------------------------");
			//System.out.println("#\tCode\tSize");
                        str = "";
			for(i=0; i<n; i++)
                            str += (i+1)+"\t"+extlist[i][1]+"\t"+extlist[i][0] + "\t";
				//System.out.println((i+1)+"\t"+extlist[i][1]+"\t"+extlist[i][0]);
			//System.out.println("\n");
                        _headerMap.put("Extensions\tCode\tSize", str);
                        
		}

        System.out.println("Create Header FIle Successful!!!");
	return;
	}
    
    public LinkedHashMap getHeader(){
        return _headerMap;
    }
    
    public void changeHeader(){
        //if (DeidData.doDeface) {
        //if (_tempPotision.getAbsolutePath().endsWith("hdr") || _tempPotision.getAbsolutePath().endsWith("img")) {
        if (!_isNiiHdr) {
            setAnalyze.descrip = new StringBuffer(_headerMap.get("Description").toString());
            setAnalyze.generated = new StringBuffer(_headerMap.get("Generated").toString());
            setAnalyze.scannum = new StringBuffer(_headerMap.get("Scannum").toString());
            setAnalyze.patient_id = new StringBuffer(_headerMap.get("Patient ID").toString());
            setAnalyze.exp_date = new StringBuffer(_headerMap.get("Exp Date").toString());
            setAnalyze.exp_time = new StringBuffer(_headerMap.get("Exp Time").toString());
            setAnalyze.hist_un0 = new StringBuffer(_headerMap.get("History").toString());
            setAnalyze.db_name = new StringBuffer(_headerMap.get("db_name").toString());
            try {
                setAnalyze.write_Analyze_Header();             
            } catch (IOException ex) {
                Logger.getLogger(NIHImage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else {//nii file
            _set.descrip = new StringBuffer(_headerMap.get("Description").toString());
            try {
                _set.writeHeader();
                _set.writeVol(_data, (short) 0);
            } catch (IOException ex) {
                Logger.getLogger(NIHImage.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        }
        emptySet();
    }
    
    
    
    public void initImage(){
        setAPI = new Nifti1Dataset(_storedPotistion.getAbsolutePath());        
    }
    public void initAnalyze() {
        try {
            setAnalyze = new Nifti1Dataset(_tempPotision.getAbsolutePath());
            short ttt = 0;
            setAnalyze.read_Analyze_Header();
            //_data = setAnalyze.readDoubleVol(ttt);
            //_data = setAnalyze.readDoubleVol(ttt);
        } catch (IOException ex) {
            Logger.getLogger(NIHImage.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    
    

    /**
     * @param storedPotistion the _storedPotistion to set
     */
    public void setStoredPotistion(File storedPotistion) {
        this._storedPotistion = storedPotistion;
    }

    /**
     * @return the _tempPotision
     */
    public File getTempPotision() {
        return _tempPotision;
    }

    /**
     * @param tempPotision the _tempPotision to set
     */
    public void setTempPotision(File tempPotision) {
        this._tempPotision = tempPotision;
    }

    /**
     * @return the _imageName
     */
    public String getImageName() {
        return _imageName;
    }

    /**
     * @param imageName the _imageName to set
     */
    public void setImageName(String imageName) {
        this._imageName = imageName;
    }

    /**
     * @return the _imageFormat
     */
    public String getImageFormat() {
        return _imageFormat;
    }

    /**
     * @param imageFormat the _imageFormat to set
     */
    public void setImageFormat(String imageFormat) {
        this._imageFormat = imageFormat;
    }

    /**
     * @return the _idInDataFile
     */
    public String getIdInDataFile() {
        return _idInDataFile;
    }

    /**
     * @param idInDataFile the _idInDataFile to set
     */
    public void setIdInDataFile(String idInDataFile) {
        this._idInDataFile = idInDataFile;
    }

    /**
     * @return the needDefaced
     */
    public boolean isNeedDefaced() {
        return _needDefaced;
    }

    /**
     * @param needDefaced the needDefaced to set
     */
    public void setNeedDefaced(boolean needDefaced) {
        this._needDefaced = needDefaced;
    }

    /**
     * @return the isDefaced
     */
    public boolean isIsDefaced() {
        return _isDefaced;
    }

    /**
     * @param isDefaced the isDefaced to set
     */
    public void setIsDefaced(boolean isDefaced) {
        this._isDefaced = isDefaced;
    }

    /**
     * @return the _imageFormalName
     */
    public String getImageFormalName() {
        return _imageFormalName;
    }

    /**
     * @param imageFormalName the _imageFormalName to set
     */
    public void setImageFormalName(String imageFormalName) {
        this._imageFormalName = imageFormalName;
    }

    public String toString() {
        return _storedPotistion.getAbsolutePath().toString();
    }

    /**
     * @return the _imageNewName
     */
    public String getImageNewName() {
        return _imageNewName;
    }

    /**
     * @param imageNewName the _imageNewName to set
     */
    public void setImageNewName(String imageNewName) {
        this._imageNewName = imageNewName;
    }

    /**
     * @return the _parentPath
     */
    public String getParentPath() {
        return _parentPath;
    }

    /**
     * @param parentPath the _parentPath to set
     */
    public void setParentPath(String parentPath) {
        this._parentPath = parentPath;
    }

    /**
     * @return the _imageDisplayName
     */
    public String getImageDisplayName() {
        return _imageDisplayName;
    }

    /**
     * @param imageDisplayName the _imageDisplayName to set
     */
    public void setImageDisplayName(String imageDisplayName) {
        this._imageDisplayName = imageDisplayName;
    }

    /**
     * @return the isLongitudinal
     */
    public boolean isIsLongitudinal() {
        return _isLongitudinal;
    }

    /**
     * @param isLongitudinal the isLongitudinal to set
     */
    public void setIsLongitudinal(boolean isLongitudinal) {
        this._isLongitudinal = isLongitudinal;
    }

    /**
     * @return the _longitudinalSuject
     */
    public String getLongitudinalSuject() {
        return _longitudinalSuject;
    }

    /**
     * @param longitudinalSuject the _longitudinalSuject to set
     */
    public void setLongitudinalSuject(String longitudinalSuject) {
        this._longitudinalSuject = longitudinalSuject;
    }

    /**
     * @return the _longitudinalNumber
     */
    public String getLongitudinalNumber() {
        return _longitudinalNumber;
    }

    /**
     * @param longitudinalNumber the _longitudinalNumber to set
     */
    public void setLongitudinalNumber(String longitudinalNumber) {
        this._longitudinalNumber = longitudinalNumber;
    }

    /**
     * @return the _seletecInJarFile
     */
    public boolean isSeletecInJarFile() {
        return _seletedInJarFile;
    }

    /**
     * @param seletecInJarFile the _seletecInJarFile to set
     */
    public void setSeletecInJarFile(boolean seletecInJarFile) {
        this._seletedInJarFile = seletecInJarFile;
    }

    /**
     * @return the _needRedefaced
     */
    public boolean isNeedRedefaced() {
        return _needRedefaced;
    }

    /**
     * @param needRedefaced the _needRedefaced to set
     */
    public void setNeedRedefaced(boolean needRedefaced) {
        this._needRedefaced = needRedefaced;
    }

    /**
     * @return the orientationState
     */
    public OrientationState getOrientationState() {
        return orientationState;
    }

    /**
     * @param orientationState the orientationState to set
     */
    public void setOrientationState(OrientationState orientationState) {
        this.orientationState = orientationState;
    }

    public BufferedImage imageAt(float sliceFactor, OrientationState orientationState) throws IOException {
  
        short[] dims = _para.dims;
        float calMax2 = _para.calMax2;
        float calMin2 = _para.calMin2;

        BufferedImage i = new BufferedImage(dims[orientationState.widthAxis], dims[orientationState.heightAxis], BufferedImage.TYPE_INT_RGB);
        int depth;
        if (orientationState.isForward) {
            depth = (int) ((dims[orientationState.currentAxis] - 1) * Math.max(0, Math.min(sliceFactor, 1.0)));
        } else {
            depth = (int) ((dims[orientationState.currentAxis] - 1) * Math.max(0, Math.min(1.0 - sliceFactor, 1.0)));
        }
        int widthStart, heightStart, heightStep;
        int widthEnd, heightEnd, widthStep;

        if (orientationState.widthPostive) {
            widthStart = 0;
            widthEnd = dims[orientationState.widthAxis] - 1;
            widthStep = 1;
        } else {
            widthStart = dims[orientationState.widthAxis] - 1;
            widthEnd = 0;
            widthStep = -1;
        }

        if (orientationState.heightPostive) {
            heightStart = 0;
            heightEnd = dims[orientationState.heightAxis] - 1;
            heightStep = 1;
        } else {
            heightStart = dims[orientationState.heightAxis] - 1;
            heightEnd = 0;
            heightStep = -1;
        }
        for (int ii = heightStart; ii != heightEnd; ii += heightStep) {
            for (int j = widthStart; j != widthEnd; j += widthStep) {

                float density = 0;

                if (orientationState.currentAxis == orientationState.Z_AXIS) {
                    if (orientationState.widthAxis == orientationState.X_AXIS) {
                        density = (float) _data[depth][ii][j];
                    } else {
                        density = (float) _data[depth][j][ii];
                    }
                } else if (orientationState.currentAxis == orientationState.Y_AXIS) {
                    if (orientationState.widthAxis == orientationState.X_AXIS) {
                        density = (float) _data[ii][depth][j];
                    } else {
                        density = (float) _data[j][depth][ii];
                    }
                } else // currentAxis is X axis
                {
                    if (orientationState.widthAxis == orientationState.Y_AXIS) {
                        density = (float) _data[ii][j][depth];
                    } else {
                        density = (float) _data[j][ii][depth];
                    }
                }
                float colorFactor = Math.min((density - calMin2) / (calMax2 - calMin2), 1f);
                int argb = new Color(colorFactor, colorFactor, colorFactor).getRGB();

                int targetX, targetY;

                if (orientationState.widthPostive) {
                    targetX = j;
                } else {
                    targetX = widthStart - j;
                }

                if (orientationState.heightPostive) {
                    targetY = ii;
                } else {
                    targetY = heightStart - ii;
                }
                //System.out.println("Width:"+i.getWidth()+"  Height:"+i.getHeight());
                //System.out.println("X:"+targetX+"  Y:"+targetY);
                i.setRGB(targetX, targetY, argb);
            }
        }
        return i;
    }

    public float getMax(double[][][] triDarr, short[] dims) {
        float max = 0;
        for (int i = 0; i < dims[2]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                for (int k = 0; k < dims[0]; k++) {
                    if (triDarr[k][j][i] > max) {
                        max = (float) triDarr[k][j][i];
                    }
                }
            }
        }
        return max;
    }

    /**
     * @return the _montageFile
     */
    public File getMontageFile() {
        return _montageFile;
    }

    /**
     * @param montageFile the _montageFile to set
     */
    public void setMontageFile(File montageFile) {
        this._montageFile = montageFile;
    }

    /**
     * @return the _isCorrectedOrientation
     */
    public boolean isIsCorrectedOrientation() {
        return _isCorrectedOrientation;
    }

    /**
     * @param isCorrectedOrientation the _isCorrectedOrientation to set
     */
    public void setIsCorrectedOrientation(boolean isCorrectedOrientation) {
        this._isCorrectedOrientation = isCorrectedOrientation;
    }

    private class NiftiPara {

        short[] dims;
        float calMax2;
        float calMin2;

        public NiftiPara(Nifti1Dataset _set) {
            dims = new short[]{_set.ZDIM, _set.YDIM, _set.XDIM};
            if (calMax2 - calMin2 == 0) {
                calMax2 = getMax(_data, dims);
                calMin2 = 0;
            }
        }
    }
}
